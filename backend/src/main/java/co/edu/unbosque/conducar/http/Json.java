package co.edu.unbosque.conducar.http;

import java.lang.reflect.RecordComponent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Conversión manual entre objetos Java y texto JSON, sin librerías externas.
 * - escribir: records, listas, mapas, textos, números, booleanos, fechas y horas.
 * - leer: lo necesario para cargar consultas.json (objetos, listas, textos, números, true/false/null).
 */
public final class Json {

    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    private Json() {
    }

    // ===================== Escritura =====================

    public static String escribir(Object valor) {
        StringBuilder sb = new StringBuilder();
        escribir(valor, sb);
        return sb.toString();
    }

    private static void escribir(Object v, StringBuilder sb) {
        if (v == null) {
            sb.append("null");
        } else if (v instanceof String s) {
            texto(s, sb);
        } else if (v instanceof Boolean b) {
            sb.append(b);
        } else if (v instanceof Double || v instanceof Float || v instanceof BigDecimal) {
            BigDecimal d = (v instanceof BigDecimal bd) ? bd : BigDecimal.valueOf(((Number) v).doubleValue());
            sb.append(d.stripTrailingZeros().toPlainString());
        } else if (v instanceof Number n) {
            sb.append(n);
        } else if (v instanceof LocalDate f) {
            texto(f.toString(), sb);                              // 2026-03-12
        } else if (v instanceof LocalTime h) {
            texto(h.format(HORA), sb);                            // 09:00:00
        } else if (v instanceof LocalDateTime fh) {
            texto(fh.toString(), sb);
        } else if (v instanceof Map<?, ?> mapa) {
            sb.append('{');
            boolean primero = true;
            for (Map.Entry<?, ?> e : mapa.entrySet()) {
                if (!primero) sb.append(',');
                texto(String.valueOf(e.getKey()), sb);
                sb.append(':');
                escribir(e.getValue(), sb);
                primero = false;
            }
            sb.append('}');
        } else if (v instanceof Collection<?> lista) {
            sb.append('[');
            boolean primero = true;
            for (Object o : lista) {
                if (!primero) sb.append(',');
                escribir(o, sb);
                primero = false;
            }
            sb.append(']');
        } else if (v instanceof Object[] arreglo) {
            escribir(List.of(arreglo), sb);
        } else if (v.getClass().isRecord()) {
            // Cada componente del record es una propiedad del objeto JSON (idAlumno, nombres, ...)
            sb.append('{');
            boolean primero = true;
            for (RecordComponent c : v.getClass().getRecordComponents()) {
                if (!primero) sb.append(',');
                texto(c.getName(), sb);
                sb.append(':');
                try {
                    escribir(c.getAccessor().invoke(v), sb);
                } catch (ReflectiveOperationException e) {
                    throw new IllegalStateException("No se pudo leer " + c.getName(), e);
                }
                primero = false;
            }
            sb.append('}');
        } else {
            texto(v.toString(), sb);
        }
    }

    private static void texto(String s, StringBuilder sb) {
        sb.append('"');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        sb.append('"');
    }

    // ===================== Lectura =====================

    /** Convierte texto JSON en Map (objeto), List (arreglo), String, Double, Boolean o null. */
    public static Object leer(String json) {
        Lector l = new Lector(json);
        Object valor = l.valor();
        l.espacios();
        if (l.pos != json.length()) {
            throw l.error("Texto sobrante");
        }
        return valor;
    }

    private static final class Lector {
        private final String s;
        private int pos;

        Lector(String s) {
            this.s = s;
        }

        Object valor() {
            espacios();
            if (pos >= s.length()) throw error("Fin inesperado");
            char c = s.charAt(pos);
            if (c == '{') return objeto();
            if (c == '[') return arreglo();
            if (c == '"') return cadena();
            if (s.startsWith("true", pos)) { pos += 4; return Boolean.TRUE; }
            if (s.startsWith("false", pos)) { pos += 5; return Boolean.FALSE; }
            if (s.startsWith("null", pos)) { pos += 4; return null; }
            return numero();
        }

        Map<String, Object> objeto() {
            Map<String, Object> mapa = new LinkedHashMap<>();
            pos++;                                   // {
            espacios();
            if (s.charAt(pos) == '}') { pos++; return mapa; }
            while (true) {
                espacios();
                String clave = cadena();
                espacios();
                esperar(':');
                mapa.put(clave, valor());
                espacios();
                if (s.charAt(pos) == ',') { pos++; continue; }
                esperar('}');
                return mapa;
            }
        }

        List<Object> arreglo() {
            List<Object> lista = new ArrayList<>();
            pos++;                                   // [
            espacios();
            if (s.charAt(pos) == ']') { pos++; return lista; }
            while (true) {
                lista.add(valor());
                espacios();
                if (s.charAt(pos) == ',') { pos++; continue; }
                esperar(']');
                return lista;
            }
        }

        String cadena() {
            esperar('"');
            StringBuilder sb = new StringBuilder();
            while (true) {
                char c = s.charAt(pos++);
                if (c == '"') return sb.toString();
                if (c != '\\') { sb.append(c); continue; }
                char e = s.charAt(pos++);
                switch (e) {
                    case 'n' -> sb.append('\n');
                    case 't' -> sb.append('\t');
                    case 'r' -> sb.append('\r');
                    case 'b' -> sb.append('\b');
                    case 'f' -> sb.append('\f');
                    case 'u' -> { sb.append((char) Integer.parseInt(s.substring(pos, pos + 4), 16)); pos += 4; }
                    default -> sb.append(e);         // \" \\ \/
                }
            }
        }

        Double numero() {
            int inicio = pos;
            while (pos < s.length() && "+-0123456789.eE".indexOf(s.charAt(pos)) >= 0) pos++;
            if (inicio == pos) throw error("Valor no válido");
            return Double.valueOf(s.substring(inicio, pos));
        }

        void esperar(char c) {
            if (pos >= s.length() || s.charAt(pos) != c) throw error("Se esperaba '" + c + "'");
            pos++;
        }

        void espacios() {
            while (pos < s.length() && Character.isWhitespace(s.charAt(pos))) pos++;
        }

        IllegalArgumentException error(String mensaje) {
            return new IllegalArgumentException(mensaje + " en la posición " + pos + " del JSON");
        }
    }
}
