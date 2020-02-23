package wolcen.salarybillsender;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataChecker {
    public DataChecker() {
        super();
    }

    public static boolean isFile(final String str) {
        return str != null && !str.isEmpty() && new File(str).exists();
    }

    public static boolean isEmail(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        str = str.trim();
        final Pattern pattern = Pattern.compile("[a-zA-Z_]{1,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}");
        final Matcher natcher = pattern.matcher(str);
        return natcher.matches();
    }

    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        str = str.trim();
        final Pattern pattern = Pattern.compile("[0-9]*");
        final Matcher natcher = pattern.matcher(str);
        return natcher.matches();
    }

    public static boolean isExcelRow(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        str = str.trim();
        if (isNumeric(str)) {
            final long row = Long.valueOf(str);
            return row > 0L;
        }
        return false;
    }

    public static boolean isExcelColumn(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        str = str.trim();
        final Pattern pattern = Pattern.compile("[A-Z]{1,2}");
        final Matcher natcher = pattern.matcher(str);
        return natcher.matches();
    }

    public static boolean isExcelCell(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        str = str.trim();
        final Pattern pattern = Pattern.compile("([A-Z]{1,2})([0-9]{1,})");
        final Matcher natcher = pattern.matcher(str);
        return natcher.matches();
    }
}
