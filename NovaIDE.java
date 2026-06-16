import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.border.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.prefs.Preferences;
import java.util.regex.*;
import java.nio.charset.StandardCharsets;
import com.sun.net.httpserver.*;

public class NovaIDE extends JFrame {

    public static class Theme {
        public String name;
        public Color BG, BG2, BG3, BG4, BG5;
        public Color BORDER, BORDER2;
        public Color ACCENT, ACCENT2, ACCENT3, GRAD1, GRAD2;
        public Color TEXT, TEXT2, TEXT3;
        public Color RED, ORANGE, YELLOW, GREEN, CYAN, BLUE, PURPLE, PINK;
        public Color C_KEYWORD, C_STRING, C_COMMENT, C_NUMBER, C_FUNCTION, C_CLASS, C_BRACKET;
        public Color SCROLLBAR, SELECTION, LINE_HIGHLIGHT;
        public Color SUCCESS, WARNING, ERROR, INFO;

        public Theme(String n, Color bg, Color bg2, Color bg3, Color bg4, Color bg5,
                     Color border, Color border2,
                     Color acc, Color acc2, Color acc3, Color g1, Color g2,
                     Color text, Color text2, Color text3,
                     Color red, Color orange, Color yellow, Color green, Color cyan, Color blue, Color purple, Color pink,
                     Color kw, Color str, Color cmt, Color num, Color fn, Color cls, Color brk,
                     Color scroll, Color sel, Color lineHl,
                     Color success, Color warning, Color error, Color info) {
            name = n;
            BG = bg; BG2 = bg2; BG3 = bg3; BG4 = bg4; BG5 = bg5;
            BORDER = border; BORDER2 = border2;
            ACCENT = acc; ACCENT2 = acc2; ACCENT3 = acc3; GRAD1 = g1; GRAD2 = g2;
            TEXT = text; TEXT2 = text2; TEXT3 = text3;
            RED = red; ORANGE = orange; YELLOW = yellow; GREEN = green;
            CYAN = cyan; BLUE = blue; PURPLE = purple; PINK = pink;
            C_KEYWORD = kw; C_STRING = str; C_COMMENT = cmt; C_NUMBER = num;
            C_FUNCTION = fn; C_CLASS = cls; C_BRACKET = brk;
            SCROLLBAR = scroll; SELECTION = sel; LINE_HIGHLIGHT = lineHl;
            SUCCESS = success; WARNING = warning; ERROR = error; INFO = info;
        }
    }

    public static Theme T;
    public static final Map<String, Theme> THEMES = new LinkedHashMap<>();
    static {
        THEMES.put("Nova Aurora", new Theme("Nova Aurora",
            new Color(0x0A0E27), new Color(0x111835), new Color(0x1A2245), new Color(0x0D1230), new Color(0x151B3D),
            new Color(0x2A3160), new Color(0x3A4280),
            new Color(0x00D9FF), new Color(0x7C3AED), new Color(0xFF00AA),
            new Color(0x00D9FF), new Color(0x7C3AED),
            new Color(0xE2E8F0), new Color(0xA0AEC0), new Color(0x718096),
            new Color(0xFF6B6B), new Color(0xFFA502), new Color(0xFFD43B), new Color(0x69DB7C),
            new Color(0x00D9FF), new Color(0x4C6EF5), new Color(0xBE4BDB), new Color(0xF783AC),
            new Color(0xFF79C6), new Color(0xA5D6FF), new Color(0x72C4A6), new Color(0xFFD580),
            new Color(0x95E1D3), new Color(0xF38181), new Color(0xAA96DA),
            new Color(0x2A3160), new Color(0x1A2F6F), new Color(0x162050),
            new Color(0x69DB7C), new Color(0xFFA502), new Color(0xFF6B6B), new Color(0x4C6EF5)
        ));
        THEMES.put("Nova Midnight", new Theme("Nova Midnight",
            new Color(0x0D0D14), new Color(0x12121C), new Color(0x18182A), new Color(0x0A0A10), new Color(0x151520),
            new Color(0x252535), new Color(0x353550),
            new Color(0x7C6AF7), new Color(0x9D8BFF), new Color(0x5A48D8),
            new Color(0x7C6AF7), new Color(0xC99EFF),
            new Color(0xCDD6F4), new Color(0x9098BB), new Color(0x555577),
            new Color(0xFF6B8A), new Color(0xFFB380), new Color(0xFFD580), new Color(0x78E89A),
            new Color(0x61D8F0), new Color(0x7EB4FF), new Color(0xC99EFF), new Color(0xFF8FA3),
            new Color(0xC99EFF), new Color(0x78E89A), new Color(0x444466), new Color(0xFFB380),
            new Color(0x7EB4FF), new Color(0xFFD580), new Color(0x61D8F0),
            new Color(0x252535), new Color(0x3A2D8A), new Color(0x1A1A2C),
            new Color(0x78E89A), new Color(0xFFD580), new Color(0xFF6B8A), new Color(0x7EB4FF)
        ));
        THEMES.put("Nova Sunset", new Theme("Nova Sunset",
            new Color(0x1A1A2E), new Color(0x212138), new Color(0x2A2A45), new Color(0x16162A), new Color(0x1E1E35),
            new Color(0x3A3A5A), new Color(0x4A4A70),
            new Color(0xFF6B6B), new Color(0xFFA07A), new Color(0xFFD93D),
            new Color(0xFF6B6B), new Color(0xFFD93D),
            new Color(0xF0F0F0), new Color(0xC0C0D0), new Color(0x9090A0),
            new Color(0xFF6B6B), new Color(0xFFA500), new Color(0xFFD43B), new Color(0x51CF66),
            new Color(0x339AF0), new Color(0x74C0FC), new Color(0xCC5DE8), new Color(0xF783AC),
            new Color(0xFF922B), new Color(0xF1C40F), new Color(0x7DCEA0), new Color(0xF39C12),
            new Color(0x85C1E2), new Color(0xBB8FCE), new Color(0xF0B27A),
            new Color(0x3A3A5A), new Color(0x4A3A5A), new Color(0x2A2540),
            new Color(0x51CF66), new Color(0xF1C40F), new Color(0xFF6B6B), new Color(0x339AF0)
        ));
    }

    // ── AUTO-COMPLETION ──────────────────────────────────────────────────────
    static class CompletionItem {
        String label, detail, snippet;
        int type;
        CompletionItem(String l, String d, String s, int t) { label=l; detail=d; snippet=s; type=t; }
    }

    static final Map<String, List<CompletionItem>> COMPLETIONS = new HashMap<>();
    static {
        List<CompletionItem> java = new ArrayList<>();
        java.add(new CompletionItem("public", "keyword", "public", 0));
        java.add(new CompletionItem("private", "keyword", "private", 0));
        java.add(new CompletionItem("protected", "keyword", "protected", 0));
        java.add(new CompletionItem("static", "keyword", "static", 0));
        java.add(new CompletionItem("void", "keyword", "void", 0));
        java.add(new CompletionItem("class", "keyword", "class", 0));
        java.add(new CompletionItem("interface", "keyword", "interface", 0));
        java.add(new CompletionItem("extends", "keyword", "extends", 0));
        java.add(new CompletionItem("implements", "keyword", "implements", 0));
        java.add(new CompletionItem("import", "keyword", "import", 0));
        java.add(new CompletionItem("package", "keyword", "package", 0));
        java.add(new CompletionItem("return", "keyword", "return", 0));
        java.add(new CompletionItem("if", "keyword", "if", 0));
        java.add(new CompletionItem("else", "keyword", "else", 0));
        java.add(new CompletionItem("for", "keyword", "for", 0));
        java.add(new CompletionItem("while", "keyword", "while", 0));
        java.add(new CompletionItem("do", "keyword", "do", 0));
        java.add(new CompletionItem("switch", "keyword", "switch", 0));
        java.add(new CompletionItem("case", "keyword", "case", 0));
        java.add(new CompletionItem("break", "keyword", "break", 0));
        java.add(new CompletionItem("continue", "keyword", "continue", 0));
        java.add(new CompletionItem("try", "keyword", "try", 0));
        java.add(new CompletionItem("catch", "keyword", "catch", 0));
        java.add(new CompletionItem("finally", "keyword", "finally", 0));
        java.add(new CompletionItem("throw", "keyword", "throw", 0));
        java.add(new CompletionItem("throws", "keyword", "throws", 0));
        java.add(new CompletionItem("new", "keyword", "new", 0));
        java.add(new CompletionItem("this", "keyword", "this", 0));
        java.add(new CompletionItem("super", "keyword", "super", 0));
        java.add(new CompletionItem("null", "keyword", "null", 0));
        java.add(new CompletionItem("true", "keyword", "true", 0));
        java.add(new CompletionItem("false", "keyword", "false", 0));
        java.add(new CompletionItem("String", "class", "String", 3));
        java.add(new CompletionItem("Integer", "class", "Integer", 3));
        java.add(new CompletionItem("Boolean", "class", "Boolean", 3));
        java.add(new CompletionItem("List", "class", "List", 3));
        java.add(new CompletionItem("Map", "class", "Map", 3));
        java.add(new CompletionItem("ArrayList", "class", "ArrayList", 3));
        java.add(new CompletionItem("HashMap", "class", "HashMap", 3));
        java.add(new CompletionItem("System.out.println", "method", "System.out.println(", 4));
        java.add(new CompletionItem("System.out.print", "method", "System.out.print(", 4));
        java.add(new CompletionItem("toString", "method", "toString()", 4));
        java.add(new CompletionItem("equals", "method", "equals(", 4));
        java.add(new CompletionItem("hashCode", "method", "hashCode()", 4));
        java.add(new CompletionItem("main", "method", "public static void main(String[] args) {\n    \n}", 4));
        COMPLETIONS.put("Java", java);

        List<CompletionItem> py = new ArrayList<>();
        py.add(new CompletionItem("def", "keyword", "def ", 0));
        py.add(new CompletionItem("class", "keyword", "class ", 0));
        py.add(new CompletionItem("if", "keyword", "if ", 0));
        py.add(new CompletionItem("elif", "keyword", "elif ", 0));
        py.add(new CompletionItem("else", "keyword", "else:", 0));
        py.add(new CompletionItem("for", "keyword", "for ", 0));
        py.add(new CompletionItem("while", "keyword", "while ", 0));
        py.add(new CompletionItem("return", "keyword", "return ", 0));
        py.add(new CompletionItem("import", "keyword", "import ", 0));
        py.add(new CompletionItem("from", "keyword", "from ", 0));
        py.add(new CompletionItem("as", "keyword", " as ", 0));
        py.add(new CompletionItem("try", "keyword", "try:", 0));
        py.add(new CompletionItem("except", "keyword", "except ", 0));
        py.add(new CompletionItem("finally", "keyword", "finally:", 0));
        py.add(new CompletionItem("with", "keyword", "with ", 0));
        py.add(new CompletionItem("lambda", "keyword", "lambda ", 0));
        py.add(new CompletionItem("yield", "keyword", "yield ", 0));
        py.add(new CompletionItem("raise", "keyword", "raise ", 0));
        py.add(new CompletionItem("pass", "keyword", "pass", 0));
        py.add(new CompletionItem("break", "keyword", "break", 0));
        py.add(new CompletionItem("continue", "keyword", "continue", 0));
        py.add(new CompletionItem("None", "keyword", "None", 0));
        py.add(new CompletionItem("True", "keyword", "True", 0));
        py.add(new CompletionItem("False", "keyword", "False", 0));
        py.add(new CompletionItem("self", "keyword", "self", 0));
        py.add(new CompletionItem("print", "function", "print(", 1));
        py.add(new CompletionItem("len", "function", "len(", 1));
        py.add(new CompletionItem("range", "function", "range(", 1));
        py.add(new CompletionItem("input", "function", "input(", 1));
        py.add(new CompletionItem("int", "function", "int(", 1));
        py.add(new CompletionItem("str", "function", "str(", 1));
        py.add(new CompletionItem("list", "function", "list(", 1));
        py.add(new CompletionItem("dict", "function", "dict(", 1));
        py.add(new CompletionItem("open", "function", "open(", 1));
        py.add(new CompletionItem("main", "function", "def main():\n    \n\nif __name__ == \"__main__\":\n    main()", 1));
        COMPLETIONS.put("Python", py);

        List<CompletionItem> js = new ArrayList<>();
        js.add(new CompletionItem("function", "keyword", "function ", 0));
        js.add(new CompletionItem("const", "keyword", "const ", 0));
        js.add(new CompletionItem("let", "keyword", "let ", 0));
        js.add(new CompletionItem("var", "keyword", "var ", 0));
        js.add(new CompletionItem("class", "keyword", "class ", 0));
        js.add(new CompletionItem("extends", "keyword", "extends ", 0));
        js.add(new CompletionItem("if", "keyword", "if ", 0));
        js.add(new CompletionItem("else", "keyword", "else ", 0));
        js.add(new CompletionItem("for", "keyword", "for ", 0));
        js.add(new CompletionItem("while", "keyword", "while ", 0));
        js.add(new CompletionItem("return", "keyword", "return ", 0));
        js.add(new CompletionItem("import", "keyword", "import ", 0));
        js.add(new CompletionItem("export", "keyword", "export ", 0));
        js.add(new CompletionItem("default", "keyword", "default ", 0));
        js.add(new CompletionItem("async", "keyword", "async ", 0));
        js.add(new CompletionItem("await", "keyword", "await ", 0));
        js.add(new CompletionItem("try", "keyword", "try {", 0));
        js.add(new CompletionItem("catch", "keyword", "catch", 0));
        js.add(new CompletionItem("finally", "keyword", "finally {", 0));
        js.add(new CompletionItem("throw", "keyword", "throw ", 0));
        js.add(new CompletionItem("new", "keyword", "new ", 0));
        js.add(new CompletionItem("this", "keyword", "this", 0));
        js.add(new CompletionItem("null", "keyword", "null", 0));
        js.add(new CompletionItem("undefined", "keyword", "undefined", 0));
        js.add(new CompletionItem("true", "keyword", "true", 0));
        js.add(new CompletionItem("false", "keyword", "false", 0));
        js.add(new CompletionItem("console.log", "function", "console.log(", 1));
        js.add(new CompletionItem("console.error", "function", "console.error(", 1));
        js.add(new CompletionItem("document.getElementById", "function", "document.getElementById(", 1));
        js.add(new CompletionItem("document.querySelector", "function", "document.querySelector(", 1));
        js.add(new CompletionItem("addEventListener", "function", "addEventListener(", 1));
        js.add(new CompletionItem("setTimeout", "function", "setTimeout(", 1));
        js.add(new CompletionItem("setInterval", "function", "setInterval(", 1));
        js.add(new CompletionItem("fetch", "function", "fetch(", 1));
        js.add(new CompletionItem("Promise", "class", "Promise", 3));
        js.add(new CompletionItem("Array", "class", "Array", 3));
        js.add(new CompletionItem("Object", "class", "Object", 3));
        js.add(new CompletionItem("Map", "class", "Map", 3));
        js.add(new CompletionItem("Set", "class", "Set", 3));
        COMPLETIONS.put("JavaScript", js);
        COMPLETIONS.put("TypeScript", js);

        List<CompletionItem> cpp = new ArrayList<>();
        cpp.add(new CompletionItem("int", "keyword", "int ", 0));
        cpp.add(new CompletionItem("void", "keyword", "void ", 0));
        cpp.add(new CompletionItem("double", "keyword", "double ", 0));
        cpp.add(new CompletionItem("float", "keyword", "float ", 0));
        cpp.add(new CompletionItem("char", "keyword", "char ", 0));
        cpp.add(new CompletionItem("bool", "keyword", "bool ", 0));
        cpp.add(new CompletionItem("auto", "keyword", "auto ", 0));
        cpp.add(new CompletionItem("const", "keyword", "const ", 0));
        cpp.add(new CompletionItem("static", "keyword", "static ", 0));
        cpp.add(new CompletionItem("class", "keyword", "class ", 0));
        cpp.add(new CompletionItem("struct", "keyword", "struct ", 0));
        cpp.add(new CompletionItem("namespace", "keyword", "namespace ", 0));
        cpp.add(new CompletionItem("using", "keyword", "using ", 0));
        cpp.add(new CompletionItem("include", "keyword", "#include ", 0));
        cpp.add(new CompletionItem("define", "keyword", "#define ", 0));
        cpp.add(new CompletionItem("if", "keyword", "if ", 0));
        cpp.add(new CompletionItem("else", "keyword", "else ", 0));
        cpp.add(new CompletionItem("for", "keyword", "for ", 0));
        cpp.add(new CompletionItem("while", "keyword", "while ", 0));
        cpp.add(new CompletionItem("return", "keyword", "return ", 0));
        cpp.add(new CompletionItem("new", "keyword", "new ", 0));
        cpp.add(new CompletionItem("delete", "keyword", "delete ", 0));
        cpp.add(new CompletionItem("nullptr", "keyword", "nullptr", 0));
        cpp.add(new CompletionItem("true", "keyword", "true", 0));
        cpp.add(new CompletionItem("false", "keyword", "false", 0));
        cpp.add(new CompletionItem("std::cout", "function", "std::cout << ", 1));
        cpp.add(new CompletionItem("std::cin", "function", "std::cin >> ", 1));
        cpp.add(new CompletionItem("std::endl", "function", "std::endl", 1));
        cpp.add(new CompletionItem("std::string", "class", "std::string", 3));
        cpp.add(new CompletionItem("std::vector", "class", "std::vector", 3));
        cpp.add(new CompletionItem("std::map", "class", "std::map", 3));
        COMPLETIONS.put("C++", cpp);
        COMPLETIONS.put("C", cpp);

        List<CompletionItem> rust = new ArrayList<>();
        rust.add(new CompletionItem("fn", "keyword", "fn ", 0));
        rust.add(new CompletionItem("let", "keyword", "let ", 0));
        rust.add(new CompletionItem("mut", "keyword", "mut ", 0));
        rust.add(new CompletionItem("pub", "keyword", "pub ", 0));
        rust.add(new CompletionItem("use", "keyword", "use ", 0));
        rust.add(new CompletionItem("mod", "keyword", "mod ", 0));
        rust.add(new CompletionItem("struct", "keyword", "struct ", 0));
        rust.add(new CompletionItem("enum", "keyword", "enum ", 0));
        rust.add(new CompletionItem("impl", "keyword", "impl ", 0));
        rust.add(new CompletionItem("trait", "keyword", "trait ", 0));
        rust.add(new CompletionItem("if", "keyword", "if ", 0));
        rust.add(new CompletionItem("else", "keyword", "else ", 0));
        rust.add(new CompletionItem("match", "keyword", "match ", 0));
        rust.add(new CompletionItem("for", "keyword", "for ", 0));
        rust.add(new CompletionItem("while", "keyword", "while ", 0));
        rust.add(new CompletionItem("loop", "keyword", "loop ", 0));
        rust.add(new CompletionItem("return", "keyword", "return ", 0));
        rust.add(new CompletionItem("self", "keyword", "self", 0));
        rust.add(new CompletionItem("Self", "keyword", "Self", 0));
        rust.add(new CompletionItem("true", "keyword", "true", 0));
        rust.add(new CompletionItem("false", "keyword", "false", 0));
        rust.add(new CompletionItem("println!", "function", "println!(\"\", )", 1));
        rust.add(new CompletionItem("print!", "function", "print!(\"\", )", 1));
        rust.add(new CompletionItem("String", "class", "String", 3));
        rust.add(new CompletionItem("Vec", "class", "Vec", 3));
        rust.add(new CompletionItem("Option", "class", "Option", 3));
        rust.add(new CompletionItem("Result", "class", "Result", 3));
        COMPLETIONS.put("Rust", rust);

        List<CompletionItem> go = new ArrayList<>();
        go.add(new CompletionItem("func", "keyword", "func ", 0));
        go.add(new CompletionItem("var", "keyword", "var ", 0));
        go.add(new CompletionItem("const", "keyword", "const ", 0));
        go.add(new CompletionItem("type", "keyword", "type ", 0));
        go.add(new CompletionItem("struct", "keyword", "struct ", 0));
        go.add(new CompletionItem("interface", "keyword", "interface ", 0));
        go.add(new CompletionItem("package", "keyword", "package ", 0));
        go.add(new CompletionItem("import", "keyword", "import ", 0));
        go.add(new CompletionItem("if", "keyword", "if ", 0));
        go.add(new CompletionItem("else", "keyword", "else ", 0));
        go.add(new CompletionItem("for", "keyword", "for ", 0));
        go.add(new CompletionItem("range", "keyword", "range ", 0));
        go.add(new CompletionItem("return", "keyword", "return ", 0));
        go.add(new CompletionItem("switch", "keyword", "switch ", 0));
        go.add(new CompletionItem("case", "keyword", "case ", 0));
        go.add(new CompletionItem("default", "keyword", "default:", 0));
        go.add(new CompletionItem("go", "keyword", "go ", 0));
        go.add(new CompletionItem("defer", "keyword", "defer ", 0));
        go.add(new CompletionItem("select", "keyword", "select ", 0));
        go.add(new CompletionItem("chan", "keyword", "chan ", 0));
        go.add(new CompletionItem("nil", "keyword", "nil", 0));
        go.add(new CompletionItem("true", "keyword", "true", 0));
        go.add(new CompletionItem("false", "keyword", "false", 0));
        go.add(new CompletionItem("fmt.Println", "function", "fmt.Println(", 1));
        go.add(new CompletionItem("fmt.Printf", "function", "fmt.Printf(", 1));
        go.add(new CompletionItem("make", "function", "make(", 1));
        go.add(new CompletionItem("append", "function", "append(", 1));
        go.add(new CompletionItem("len", "function", "len(", 1));
        go.add(new CompletionItem("main", "function", "func main() {\n    \n}", 1));
        COMPLETIONS.put("Go", go);

        List<CompletionItem> html = new ArrayList<>();
        html.add(new CompletionItem("<html>", "tag", "<html>\n    \n</html>", 5));
        html.add(new CompletionItem("<head>", "tag", "<head>\n    \n</head>", 5));
        html.add(new CompletionItem("<body>", "tag", "<body>\n    \n</body>", 5));
        html.add(new CompletionItem("<div>", "tag", "<div>\n    \n</div>", 5));
        html.add(new CompletionItem("<span>", "tag", "<span></span>", 5));
        html.add(new CompletionItem("<p>", "tag", "<p></p>", 5));
        html.add(new CompletionItem("<a>", "tag", "<a href=\"\"></a>", 5));
        html.add(new CompletionItem("<img>", "tag", "<img src=\"\" alt=\"\">", 5));
        html.add(new CompletionItem("<button>", "tag", "<button></button>", 5));
        html.add(new CompletionItem("<input>", "tag", "<input type=\"\" name=\"\">", 5));
        html.add(new CompletionItem("<form>", "tag", "<form>\n    \n</form>", 5));
        html.add(new CompletionItem("<script>", "tag", "<script>\n    \n</script>", 5));
        html.add(new CompletionItem("<style>", "tag", "<style>\n    \n</style>", 5));
        html.add(new CompletionItem("<link>", "tag", "<link rel=\"stylesheet\" href=\"\">", 5));
        html.add(new CompletionItem("<meta>", "tag", "<meta name=\"\" content=\"\">", 5));
        html.add(new CompletionItem("<title>", "tag", "<title></title>", 5));
        html.add(new CompletionItem("<h1>", "tag", "<h1></h1>", 5));
        html.add(new CompletionItem("<h2>", "tag", "<h2></h2>", 5));
        html.add(new CompletionItem("<ul>", "tag", "<ul>\n    <li></li>\n</ul>", 5));
        html.add(new CompletionItem("<ol>", "tag", "<ol>\n    <li></li>\n</ol>", 5));
        html.add(new CompletionItem("<table>", "tag", "<table>\n    \n</table>", 5));
        COMPLETIONS.put("HTML/CSS/JS", html);
    }

    // ── PATTERN CACHE ────────────────────────────────────────────────────────
    private static final Map<String, Pattern> PATTERN_CACHE = new HashMap<>();
    private static Pattern cached(String regex) {
        return PATTERN_CACHE.computeIfAbsent(regex, Pattern::compile);
    }

    // ── STATE ────────────────────────────────────────────────────────────────
    private JTabbedPane tabs;
    private JTextPane terminal;
    private StyledDocument termDoc;
    private JLabel statusLbl, liveLbl, cursorLbl;
    private JComboBox<String> langBox, runModeBox, themeBox;
    private final Map<JTextPane, File> editorFiles = new WeakHashMap<>();
    private final Map<JTextPane, Boolean> editorModified = new WeakHashMap<>();
    private Process runProcess;
    private HttpServer liveServer;
    private boolean liveOn = false;
    private javax.swing.Timer hlTimer;
    private JTree fileTree;
    private DefaultTreeModel treeModel;
    private File projectRoot;
    private JTextPane activeEd;
    private CompletionPopup completionPopup;
    private Point dragStart;
    private JButton activeActivityBtn;

    private static class Clipboard {
        enum Op { COPY, CUT }
        Op op; File src;
        void copy(File f) { op=Op.COPY; src=f; }
        void cut(File f)  { op=Op.CUT;  src=f; }
        void clear() { src=null; }
        boolean has() { return src!=null; }
        boolean isCopy() { return op==Op.COPY; }
    }
    private final Clipboard clip = new Clipboard();

    // ── LANGUAGES ────────────────────────────────────────────────────────────
    static final class Lang {
        final String name; final String[] exts; final String icon;
        Lang(String n, String[] e, String i) { name=n; exts=e; icon=i; }
        boolean matches(String f) { for (String ex : exts) if (f.endsWith(ex)) return true; return false; }
    }
    static final Map<String, Lang> LANGS = new LinkedHashMap<>();
    static {
        LANGS.put("Java",        new Lang("Java",       new String[]{".java"},             "☕"));
        LANGS.put("Python",      new Lang("Python",     new String[]{".py",".pyw"},        "🐍"));
        LANGS.put("JavaScript",  new Lang("JavaScript", new String[]{".js",".mjs"},        "⚡"));
        LANGS.put("TypeScript",  new Lang("TypeScript", new String[]{".ts",".tsx"},        "📘"));
        LANGS.put("C",           new Lang("C",          new String[]{".c"},                "🔷"));
        LANGS.put("C++",         new Lang("C++",        new String[]{".cpp",".cc",".cxx"}, "🔷"));
        LANGS.put("Rust",        new Lang("Rust",       new String[]{".rs"},               "🦀"));
        LANGS.put("Go",          new Lang("Go",         new String[]{".go"},               "🐹"));
        LANGS.put("Kotlin",      new Lang("Kotlin",     new String[]{".kt"},               "🎯"));
        LANGS.put("Ruby",        new Lang("Ruby",       new String[]{".rb"},               "💎"));
        LANGS.put("PHP",         new Lang("PHP",        new String[]{".php"},              "🐘"));
        LANGS.put("Bash",        new Lang("Bash",       new String[]{".sh",".bash"},       "📟"));
        LANGS.put("Lua",         new Lang("Lua",        new String[]{".lua"},              "🌙"));
        LANGS.put("HTML/CSS/JS", new Lang("HTML/CSS/JS",new String[]{".html",".css",".htm"},"🌐"));
    }
    static Lang getLang(String f) {
        for (Lang l : LANGS.values()) if (l.matches(f)) return l;
        return LANGS.get("HTML/CSS/JS");
    }

    static final Map<String, String> TEMPLATES = new LinkedHashMap<>();
    static {
        TEMPLATES.put("Java",        "public class Main {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, Nova!\");\n    }\n}\n");
        TEMPLATES.put("Python",      "def main():\n    print(\"Hello, Nova!\")\n\nif __name__ == \"__main__\":\n    main()\n");
        TEMPLATES.put("JavaScript",  "const greet = (name) => `Hello, ${name}!`;\nconsole.log(greet('Nova'));\n");
        TEMPLATES.put("C++",         "#include <iostream>\n\nint main() {\n    std::cout << \"Hello, Nova!\" << std::endl;\n    return 0;\n}\n");
        TEMPLATES.put("Rust",        "fn main() {\n    println!(\"Hello, Nova!\");\n}\n");
        TEMPLATES.put("Go",          "package main\n\nimport \"fmt\"\n\nfunc main() {\n    fmt.Println(\"Hello, Nova!\")\n}\n");
        TEMPLATES.put("HTML/CSS/JS", "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n    <meta charset=\"UTF-8\">\n    <title>Nova</title>\n</head>\n<body>\n    <h1>Hello, Nova!</h1>\n</body>\n</html>\n");
    }

    // ── SETTINGS ─────────────────────────────────────────────────────────────
    static final class Cfg {
        private static final Preferences p = Preferences.userNodeForPackage(Cfg.class);
        static boolean isWin() { return System.getProperty("os.name").toLowerCase().contains("win"); }
        static boolean cmdExists(String c) {
            try {
                return new ProcessBuilder(isWin() ? new String[]{"where", c} : new String[]{"which", c})
                    .redirectErrorStream(true).start().waitFor() == 0;
            } catch (Exception e) { return false; }
        }
        static String findCmd(String... cmds) {
            for (String c : cmds) if (cmdExists(c)) return c;
            return cmds[0];
        }
        static int getLivePort() { return p.getInt("livePort", 5500); }
        static String getLastDir() { return p.get("lastDir", System.getProperty("user.home")); }
        static void setLastDir(String d) { p.put("lastDir", d); }
        static String getTheme() { return p.get("theme", "Nova Aurora"); }
        static void setTheme(String t) { p.put("theme", t); }
    }

    // ── CONSTRUCTOR ──────────────────────────────────────────────────────────
    public NovaIDE() {
        super("Nova IDE");
        T = THEMES.getOrDefault(Cfg.getTheme(), THEMES.get("Nova Aurora"));
        setUndecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1600, 1000);
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);
        buildUI();
        setupShortcuts();
        setVisible(true);
        printWelcome();
        new Thread(this::checkTools, "ToolCheck").start();
    }

    // ── UI ASSEMBLY ──────────────────────────────────────────────────────────
    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(T.BG);
        root.setBorder(BorderFactory.createLineBorder(T.BORDER, 1));

        root.add(buildTitleBar(), BorderLayout.NORTH);

        JPanel middle = new JPanel(new BorderLayout());
        middle.setOpaque(false);
        middle.add(buildActivityBar(), BorderLayout.WEST);

        JSplitPane main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildSidebar(), buildWorkArea());
        main.setResizeWeight(0.18);
        main.setDividerSize(1);
        main.setBackground(T.BORDER);
        main.setBorder(null);
        middle.add(main, BorderLayout.CENTER);

        root.add(middle, BorderLayout.CENTER);
        root.add(buildStatusBar(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    // ── TITLE BAR ────────────────────────────────────────────────────────────
    private JPanel buildTitleBar() {
        JPanel bar = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, T.BG4, getWidth(), 0, T.BG3);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        bar.setOpaque(false);
        bar.setPreferredSize(new Dimension(0, 44));

        // Dragging
        MouseAdapter drag = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (getExtendedState() == JFrame.MAXIMIZED_BOTH) return;
                dragStart = e.getPoint();
            }
            public void mouseDragged(MouseEvent e) {
                if (dragStart == null) return;
                Point p = getLocation();
                setLocation(p.x + e.getX() - dragStart.x, p.y + e.getY() - dragStart.y);
            }
        };
        bar.addMouseListener(drag);
        bar.addMouseMotionListener(drag);

        // Left: Logo + Menu
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        left.setOpaque(false);

        JLabel logo = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, T.GRAD1, getWidth(), getHeight(), T.GRAD2);
                g2.setPaint(gp);
                int[] xs = {10,20,20,10,0,0}; int[] ys = {0,5,15,20,15,5};
                g2.fillPolygon(xs, ys, 6);
                g2.setColor(T.BG4);
                g2.setFont(new Font("Monospaced", Font.BOLD, 10));
                g2.drawString("N", 5, 14);
                g2.dispose();
            }
        };
        logo.setPreferredSize(new Dimension(24, 22));
        left.add(logo);

        JLabel name = new JLabel("Nova IDE");
        name.setFont(new Font("Dialog", Font.BOLD, 13));
        name.setForeground(T.TEXT);
        left.add(name);

        left.add(Box.createHorizontalStrut(16));
        for (String m : new String[]{"File","Edit","View","Go","Run","Terminal","Help"}) {
            JLabel ml = new JLabel(m) {
                @Override protected void paintComponent(Graphics g) {
                    if (getModel() != null) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        if (getModel().isRollover()) {
                            g2.setColor(new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 30));
                            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                        }
                        g2.dispose();
                    }
                    super.paintComponent(g);
                }
            };
            ml.setFont(new Font("Dialog", Font.PLAIN, 12));
            ml.setForeground(T.TEXT2);
            ml.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            ml.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            ml.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { ml.setForeground(T.TEXT); ml.repaint(); }
                public void mouseExited(MouseEvent e)  { ml.setForeground(T.TEXT2); ml.repaint(); }
                public void mouseClicked(MouseEvent e) { handleMenu(m); }
            });
            left.add(ml);
        }
        bar.add(left, BorderLayout.WEST);

        // Center: Command Palette
        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        center.setOpaque(false);
        JTextField cmdPalette = new JTextField("⌘  Type to command... (Ctrl+Shift+P)") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(T.BG.getRed(), T.BG.getGreen(), T.BG.getBlue(), 180));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(T.BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        cmdPalette.setFont(new Font("Dialog", Font.PLAIN, 12));
        cmdPalette.setForeground(T.TEXT3);
        cmdPalette.setBackground(new Color(0,0,0,0));
        cmdPalette.setOpaque(false);
        cmdPalette.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        cmdPalette.setPreferredSize(new Dimension(380, 28));
        cmdPalette.setHorizontalAlignment(SwingConstants.CENTER);
        cmdPalette.setEditable(false);
        cmdPalette.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cmdPalette.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { showCommandPalette(); }
        });
        center.add(cmdPalette);
        bar.add(center, BorderLayout.CENTER);

        // Right: Window controls
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(winBtn("─", false, e -> setState(ICONIFIED)));
        right.add(winBtn("▢", false, e -> {
            if (getExtendedState() == MAXIMIZED_BOTH) setExtendedState(NORMAL);
            else setExtendedState(MAXIMIZED_BOTH);
        }));
        right.add(winBtn("✕", true, e -> {
            int r = JOptionPane.showConfirmDialog(this, "Exit Nova IDE?", "Exit", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) System.exit(0);
        }));
        bar.add(right, BorderLayout.EAST);

        return bar;
    }

    private JButton winBtn(String icon, boolean danger, ActionListener a) {
        JButton b = new JButton(icon) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(danger ? T.RED : new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 40));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    setForeground(danger ? Color.WHITE : T.TEXT);
                } else setForeground(T.TEXT2);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Dialog", Font.PLAIN, 14));
        b.setBackground(null); b.setContentAreaFilled(false); b.setBorderPainted(false); b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(46, 44));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(a);
        return b;
    }

    private void handleMenu(String m) {
        switch (m) {
            case "File" -> {
                String[] opts = {"New File", "Open File", "Open Folder", "Save", "Save As", "Close Tab", "Exit"};
                int r = JOptionPane.showOptionDialog(this, "Choose action:", "File", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opts, opts[0]);
                if (r == 0) newTab();
                else if (r == 1) openFile();
                else if (r == 2) openProject();
                else if (r == 3) saveFile();
                else if (r == 4) saveFileAs();
                else if (r == 5) closeCurrentTab();
                else if (r == 6) System.exit(0);
            }
            case "Edit" -> {
                String[] opts = {"Find (Ctrl+F)", "Replace", "Go to Line", "Quick Open (Ctrl+P)"};
                int r = JOptionPane.showOptionDialog(this, "Choose action:", "Edit", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opts, opts[0]);
                if (r == 0) showFind();
                else if (r == 1) showReplace();
                else if (r == 2) showGotoLine();
                else if (r == 3) showQuickOpen();
            }
            case "View" -> {
                String[] opts = {"Toggle Sidebar", "Toggle Terminal", "Change Theme", "Zoom In", "Zoom Out"};
                int r = JOptionPane.showOptionDialog(this, "Choose action:", "View", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opts, opts[0]);
                if (r == 2) cycleTheme();
            }
            case "Run" -> runCode();
            case "Terminal" -> tprint("Terminal ready.\n", T.INFO);
            case "Help" -> {
                JOptionPane.showMessageDialog(this, "Nova IDE v2.0\n\nCtrl+Enter: Run\nCtrl+S: Save\nCtrl+P: Quick Open\nCtrl+Space: Autocomplete\nCtrl+Shift+P: Command Palette\nCtrl+F: Find\nCtrl+G: Go to Line", "About", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    // ── ACTIVITY BAR ─────────────────────────────────────────────────────────
    private JPanel buildActivityBar() {
        JPanel bar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, T.BG4, 0, getHeight(), T.BG3);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(T.BORDER);
                g2.drawLine(getWidth()-1, 0, getWidth()-1, getHeight());
                g2.dispose();
            }
        };
        bar.setLayout(new BoxLayout(bar, BoxLayout.Y_AXIS));
        bar.setOpaque(false);
        bar.setPreferredSize(new Dimension(56, 0));

        String[][] items = {{"📁","Explorer"},{"🔍","Search"},{"🌿","Git"},{"🐞","Debug"},{"📦","Extensions"}};
        for (String[] it : items) {
            bar.add(Box.createVerticalStrut(6));
            bar.add(activityBtn(it[0], it[1]));
        }
        bar.add(Box.createVerticalGlue());
        bar.add(activityBtn("⚙", "Settings"));
        bar.add(Box.createVerticalStrut(10));
        return bar;
    }

    private JButton activityBtn(String icon, String tooltip) {
        JButton b = new JButton(icon) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (activeActivityBtn == this) {
                    GradientPaint gp = new GradientPaint(0, 0, new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 50), getWidth(), getHeight(), new Color(T.ACCENT2.getRed(), T.ACCENT2.getGreen(), T.ACCENT2.getBlue(), 30));
                    g2.setPaint(gp);
                    g2.fillRoundRect(6, 4, getWidth()-12, getHeight()-8, 8, 8);
                    g2.setColor(T.ACCENT);
                    g2.fillRect(0, 10, 3, getHeight()-20);
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 25));
                    g2.fillRoundRect(6, 4, getWidth()-12, getHeight()-8, 8, 8);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        b.setForeground(T.TEXT3);
        b.setBackground(null);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(56, 52));
        b.setMaximumSize(new Dimension(56, 52));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setToolTipText(tooltip);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { if (activeActivityBtn != b) b.setForeground(T.TEXT); b.repaint(); }
            public void mouseExited(MouseEvent e)  { if (activeActivityBtn != b) b.setForeground(T.TEXT3); b.repaint(); }
        });
        b.addActionListener(e -> {
            activeActivityBtn = b;
            b.getParent().repaint();
            tprint("📍 " + tooltip + " panel\n", T.INFO);
        });
        return b;
    }

    // ── SIDEBAR ──────────────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(T.BG2);
        panel.setPreferredSize(new Dimension(280, 0));
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, T.BORDER));

        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBackground(T.BG2);
        hdr.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 12));
        JLabel lbl = new JLabel("EXPLORER");
        lbl.setFont(new Font("Dialog", Font.BOLD, 11));
        lbl.setForeground(T.ACCENT);
        hdr.add(lbl, BorderLayout.WEST);

        JPanel hdrBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        hdrBtns.setBackground(T.BG2);
        for (String[] ic : new String[][]{{"⊕","New File"},{"⊞","New Folder"},{"↻","Refresh"}}) {
            JLabel btn = new JLabel(ic[0]) {
                @Override protected void paintComponent(Graphics g) {
                    if (getModel() != null && getModel().isRollover()) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setColor(new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 30));
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                        g2.dispose();
                    }
                    super.paintComponent(g);
                }
            };
            btn.setFont(new Font("Dialog", Font.PLAIN, 16));
            btn.setForeground(T.TEXT3);
            btn.setToolTipText(ic[1]);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { btn.setForeground(T.ACCENT); btn.repaint(); }
                public void mouseExited(MouseEvent e)  { btn.setForeground(T.TEXT3); btn.repaint(); }
                public void mouseClicked(MouseEvent e) {
                    if (ic[1].equals("New File"))        { if (projectRoot!=null) createNewFile(projectRoot); }
                    else if (ic[1].equals("New Folder")) { if (projectRoot!=null) createNewFolder(projectRoot); }
                    else refreshTree();
                }
            });
            hdrBtns.add(btn);
        }
        hdr.add(hdrBtns, BorderLayout.EAST);
        panel.add(hdr, BorderLayout.NORTH);

        fileTree = new JTree(new DefaultTreeModel(new DefaultMutableTreeNode("No folder opened")));
        fileTree.setBackground(T.BG2);
        fileTree.setForeground(T.TEXT2);
        fileTree.setFont(new Font("Dialog", Font.PLAIN, 13));
        fileTree.setBorder(null);
        fileTree.setRowHeight(26);
        fileTree.setCellRenderer(new NovaTreeRenderer());
        fileTree.setShowsRootHandles(true);
        fileTree.addMouseListener(new TreeCtxListener());
        fileTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
            if (node != null && node.getUserObject() instanceof File f && f.isFile())
                openFileInEditor(f);
        });

        JScrollPane tsp = new JScrollPane(fileTree);
        tsp.setBorder(null);
        tsp.getViewport().setBackground(T.BG2);
        tsp.getVerticalScrollBar().setUI(new ThinScrollBarUI());
        panel.add(tsp, BorderLayout.CENTER);
        return panel;
    }

    static class ThinScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override protected void configureScrollBarColors() {
            thumbColor = new Color(T.SCROLLBAR.getRed(), T.SCROLLBAR.getGreen(), T.SCROLLBAR.getBlue(), 180);
            trackColor = new Color(0, 0, 0, 0);
        }
        @Override protected JButton createDecreaseButton(int o) { JButton b = new JButton(); b.setPreferredSize(new Dimension(0,0)); return b; }
        @Override protected JButton createIncreaseButton(int o) { JButton b = new JButton(); b.setPreferredSize(new Dimension(0,0)); return b; }
        @Override protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            Color thumb = new Color(T.SCROLLBAR.getRed(), T.SCROLLBAR.getGreen(), T.SCROLLBAR.getBlue(), 180);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumb);
            g2.fillRoundRect(r.x+2, r.y, r.width-4, r.height, 6, 6);
            g2.dispose();
        }
        @Override protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            g.setColor(new Color(0,0,0,0)); g.fillRect(r.x, r.y, r.width, r.height);
        }
    }

    class NovaTreeRenderer extends DefaultTreeCellRenderer {
        @Override public Component getTreeCellRendererComponent(
                JTree tree, Object val, boolean sel, boolean exp, boolean leaf, int row, boolean focus) {
            super.getTreeCellRendererComponent(tree, val, sel, exp, leaf, row, focus);
            setOpaque(true);
            setBackgroundSelectionColor(new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 50));
            setBackgroundNonSelectionColor(T.BG2);
            setBorderSelectionColor(new Color(0,0,0,0));
            setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));
            setFont(new Font("Dialog", Font.PLAIN, 13));

            if (val instanceof DefaultMutableTreeNode node) {
                Object uo = node.getUserObject();
                if (uo instanceof File f) {
                    if (f.isDirectory()) { setIcon(createFolderIcon(exp)); setForeground(sel ? T.TEXT : T.TEXT2); }
                    else                  { setIcon(createFileIcon(f.getName())); setForeground(T.TEXT); }
                    setText(f.getName());
                } else { setText("  " + uo.toString()); setForeground(T.TEXT3); }
            }
            setBackground(sel ? new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 50) : new Color(0,0,0,0));
            return this;
        }

        private ImageIcon createFolderIcon(boolean open) {
            BufferedImage img = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color c = open ? T.ACCENT : T.YELLOW;
            GradientPaint gp = new GradientPaint(0, 0, c, 20, 20, new Color(c.getRed(), c.getGreen(), c.getBlue(), 180));
            g2.setPaint(gp);
            g2.fillRoundRect(1, 5, 17, 12, 3, 3);
            g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 220));
            g2.fillRoundRect(3, 3, 8, 4, 2, 2);
            g2.dispose();
            return new ImageIcon(img);
        }

        private ImageIcon createFileIcon(String name) {
            BufferedImage img = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color c = getFileColor(name);
            g2.setColor(c);
            g2.fillRoundRect(3, 2, 13, 16, 2, 2);
            g2.setColor(new Color(0,0,0,50));
            g2.fillPolygon(new int[]{12,16,16,12}, new int[]{2,6,6,2}, 4);
            g2.setColor(new Color(255,255,255,120));
            g2.drawLine(6, 10, 13, 10);
            g2.drawLine(6, 13, 13, 13);
            g2.dispose();
            return new ImageIcon(img);
        }

        private Color getFileColor(String name) {
            String n = name.toLowerCase();
            if (n.endsWith(".java"))  return new Color(255,152,0);
            if (n.endsWith(".py") || n.endsWith(".pyw"))  return new Color(53,114,165);
            if (n.endsWith(".js") || n.endsWith(".mjs"))  return new Color(240,210,80);
            if (n.endsWith(".ts") || n.endsWith(".tsx"))  return new Color(49,120,198);
            if (n.endsWith(".html") || n.endsWith(".htm")) return new Color(227,76,38);
            if (n.endsWith(".css"))  return new Color(21,114,187);
            if (n.endsWith(".json")) return new Color(255,206,102);
            if (n.endsWith(".md"))   return new Color(100,140,180);
            if (n.endsWith(".rs"))   return new Color(220,100,60);
            if (n.endsWith(".go"))   return new Color(0,173,216);
            if (n.endsWith(".cpp") || n.endsWith(".cc") || n.endsWith(".cxx") || n.endsWith(".c") || n.endsWith(".h")) return new Color(0,89,155);
            if (n.endsWith(".kt"))   return new Color(160,120,220);
            if (n.endsWith(".rb"))   return new Color(200,60,60);
            if (n.endsWith(".php"))  return new Color(120,130,200);
            if (n.endsWith(".sh") || n.endsWith(".bash")) return new Color(100,160,100);
            if (n.endsWith(".lua"))  return new Color(0,80,180);
            return T.TEXT3;
        }
    }

    class TreeCtxListener extends MouseAdapter {
        public void mousePressed(MouseEvent e)  { check(e); }
        public void mouseReleased(MouseEvent e) { check(e); }
        private void check(MouseEvent e) {
            if (!e.isPopupTrigger() || projectRoot==null) return;
            int row = fileTree.getRowForLocation(e.getX(), e.getY());
            if (row != -1) fileTree.setSelectionRow(row);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
            if (node==null || !(node.getUserObject() instanceof File f)) return;
            showTreeCtx(f, e.getX(), e.getY());
        }
    }

    private void showTreeCtx(File f, int x, int y) {
        JPopupMenu pm = new JPopupMenu();
        pm.setBackground(T.BG3);
        pm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(T.ACCENT, 1),
            BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        addCtxItem(pm, "📄 New File",   () -> createNewFile(f.isDirectory() ? f : f.getParentFile()));
        addCtxItem(pm, "📁 New Folder", () -> createNewFolder(f.isDirectory() ? f : f.getParentFile()));
        pm.addSeparator();
        addCtxItem(pm, "📋 Copy", () -> clip.copy(f));
        addCtxItem(pm, "✂️ Cut",  () -> clip.cut(f));
        JMenuItem paste = ctxItem("📌 Paste");
        paste.setEnabled(clip.has());
        paste.addActionListener(e -> pasteToDir(f.isDirectory() ? f : f.getParentFile()));
        pm.add(paste);
        pm.addSeparator();
        addCtxItem(pm, "✏️ Rename", () -> renameFile(f));
        addCtxItem(pm, "🗑️ Delete", () -> deleteFile(f));
        pm.addSeparator();
        addCtxItem(pm, "📂 Reveal in Explorer", () -> {
            try { Desktop.getDesktop().open(f.isDirectory() ? f : f.getParentFile()); } catch (Exception ex) {}
        });
        pm.show(fileTree, x, y);
    }

    private JMenuItem ctxItem(String t) {
        JMenuItem m = new JMenuItem(t);
        m.setBackground(T.BG3); m.setForeground(T.TEXT);
        m.setFont(new Font("Dialog", Font.PLAIN, 12));
        m.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        m.setOpaque(true);
        m.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                m.setBackground(new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 60));
            }
            public void mouseExited(MouseEvent e) { m.setBackground(T.BG3); }
        });
        return m;
    }

    private void addCtxItem(JPopupMenu pm, String t, Runnable r) {
        JMenuItem m = ctxItem(t); m.addActionListener(e -> r.run()); pm.add(m);
    }

    // ── WORK AREA ────────────────────────────────────────────────────────────
    private JSplitPane buildWorkArea() {
        tabs = new JTabbedPane(JTabbedPane.TOP) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, T.BG, 0, getHeight(), T.BG2);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tabs.setBackground(T.BG2);
        tabs.setForeground(T.TEXT2);
        tabs.setFont(new Font("Dialog", Font.PLAIN, 12));
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        UIManager.put("TabbedPane.tabsOverlapBorder", true);

        tabs.addChangeListener(e -> {
            int i = tabs.getSelectedIndex();
            if (i < 0) return;
            Component c = tabs.getComponentAt(i);
            if (c instanceof JScrollPane sp && sp.getViewport().getView() instanceof JTextPane tp)
                activeEd = tp;
        });

        addNewTab("untitled.java", TEMPLATES.get("Java"), null);

        JSplitPane vSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabs, buildTermPanel());
        vSplit.setResizeWeight(0.72);
        vSplit.setDividerSize(3);
        vSplit.setBackground(T.BORDER);
        vSplit.setBorder(null);
        return vSplit;
    }

    // ── EDITOR TABS ──────────────────────────────────────────────────────────
    private void addNewTab(String name, String content, File sourceFile) {
        JTextPane ed = new JTextPane() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(T.BG);
                g2.fillRect(0, 0, getWidth(), getHeight());
                try {
                    Rectangle r = modelToView2D(getCaretPosition()).getBounds();
                    GradientPaint gp = new GradientPaint(0, r.y, new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 15), 0, r.y + r.height, new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 5));
                    g2.setPaint(gp);
                    g2.fillRect(0, r.y, getWidth(), r.height);
                } catch (Exception ignored) {}
                g2.dispose();
                super.paintComponent(g);
            }
        };
        ed.setBackground(T.BG);
        ed.setForeground(T.TEXT);
        ed.setCaretColor(T.ACCENT);
        ed.setSelectionColor(new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 60));
        ed.setFont(new Font("Consolas", Font.PLAIN, 14));
        ed.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        ed.setText(content);
        ed.setCaretPosition(0);

        if (sourceFile != null) editorFiles.put(ed, sourceFile);
        editorModified.put(ed, false);

        ed.getInputMap().put(KeyStroke.getKeyStroke("TAB"), new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                try { ed.getDocument().insertString(ed.getCaretPosition(), "    ", null); }
                catch (BadLocationException ignored) {}
            }
        });

        // Ctrl+Space for autocomplete
        ed.getInputMap().put(KeyStroke.getKeyStroke("control SPACE"), "autocomplete");
        ed.getActionMap().put("autocomplete", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { showAutocomplete(ed); }
        });

        // Auto-trigger autocomplete on typing
        ed.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                scheduleHl(ed); updateCursor(ed);
                editorModified.put(ed, true);
                updateTabTitle(ed);
                try {
                    String inserted = ed.getDocument().getText(e.getOffset(), e.getLength());
                    if (inserted.length() == 1 && Character.isJavaIdentifierPart(inserted.charAt(0))) {
                        SwingUtilities.invokeLater(() -> showAutocomplete(ed));
                    } else {
                        if (completionPopup != null && completionPopup.isVisible()) completionPopup.hideCompletion();
                    }
                } catch (Exception ex) {}
            }
            public void removeUpdate(DocumentEvent e) {
                scheduleHl(ed); updateCursor(ed);
                editorModified.put(ed, true);
                updateTabTitle(ed);
            }
            public void changedUpdate(DocumentEvent e) {}
        });
        ed.addCaretListener(e -> updateCursor(ed));

        LineGutter gutter = new LineGutter(ed);
        JScrollPane sp = new JScrollPane(ed);
        sp.setRowHeaderView(gutter);
        sp.setBorder(null);
        sp.getViewport().setBackground(T.BG);
        sp.getVerticalScrollBar().setUI(new ThinScrollBarUI());
        sp.getHorizontalScrollBar().setUI(new ThinScrollBarUI());

        int idx = tabs.getTabCount();
        tabs.addTab(name, sp);
        tabs.setTabComponentAt(idx, makeTabComp(name, idx, ed));
        tabs.setSelectedIndex(idx);
        activeEd = ed;
    }

    private JPanel makeTabComp(String name, int idx, JTextPane ed) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (tabs.getSelectedComponent() == ((JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, ed))) {
                    GradientPaint gp = new GradientPaint(0, 0, new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 30), getWidth(), 0, new Color(T.ACCENT2.getRed(), T.ACCENT2.getGreen(), T.ACCENT2.getBlue(), 15));
                    g2.setPaint(gp);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(T.ACCENT);
                    g2.fillRect(0, getHeight()-2, getWidth(), 2);
                } else {
                    g2.setColor(new Color(0, 0, 0, 0));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
                g2.dispose();
            }
        };
        p.setOpaque(false); p.setBorder(null);

        JLabel ico = new JLabel(getLang(name).icon);
        ico.setFont(new Font("Dialog", Font.PLAIN, 12));
        p.add(ico);

        JLabel lbl = new JLabel(name);
        lbl.setFont(new Font("Dialog", Font.PLAIN, 12));
        lbl.setForeground(T.TEXT2);
        p.add(lbl);

        JButton x = new JButton("×") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(T.RED);
                    g2.fillOval(0, 0, getWidth(), getHeight());
                    setForeground(Color.WHITE);
                } else {
                    setForeground(T.TEXT3);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        x.setFont(new Font("Dialog", Font.BOLD, 12));
        x.setBackground(null);
        x.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
        x.setContentAreaFilled(false); x.setFocusPainted(false);
        x.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        x.setPreferredSize(new Dimension(18, 18));
        x.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { x.repaint(); }
            public void mouseExited(MouseEvent e)  { x.repaint(); }
        });
        x.addActionListener(e -> {
            for (int i = 0; i < tabs.getTabCount(); i++) {
                Component comp = tabs.getComponentAt(i);
                if (comp instanceof JScrollPane sp && sp.getViewport().getView() == ed) {
                    editorFiles.remove(ed);
                    editorModified.remove(ed);
                    tabs.remove(i);
                    break;
                }
            }
        });
        p.add(x);
        return p;
    }

    private void updateTabTitle(JTextPane ed) {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            Component comp = tabs.getComponentAt(i);
            if (comp instanceof JScrollPane sp && sp.getViewport().getView() == ed) {
                Boolean mod = editorModified.get(ed);
                String title = tabs.getTitleAt(i);
                if (mod != null && mod && !title.startsWith("●")) {
                    tabs.setTitleAt(i, "● " + title);
                }
                break;
            }
        }
    }

    private void newTab() { addNewTab("untitled.java", TEMPLATES.get("Java"), null); }

    private void closeCurrentTab() {
        int i = tabs.getSelectedIndex();
        if (i >= 0) {
            Component comp = tabs.getComponentAt(i);
            if (comp instanceof JScrollPane sp && sp.getViewport().getView() instanceof JTextPane tp) {
                editorFiles.remove(tp);
                editorModified.remove(tp);
            }
            tabs.remove(i);
        }
    }

    // ── AUTOCOMPLETE POPUP ───────────────────────────────────────────────────
    class CompletionPopup extends JWindow {
        private JList<CompletionItem> list;
        private DefaultListModel<CompletionItem> model;
        private JTextPane editor;
        private JLabel detailLabel;

        CompletionPopup(JFrame parent) {
            super(parent);
            setBackground(new Color(0,0,0,0));

            JPanel content = new JPanel(new BorderLayout()) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(T.BG3);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.setColor(new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 100));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                    g2.dispose();
                }
            };
            content.setOpaque(false);
            content.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

            model = new DefaultListModel<>();
            list = new JList<>(model);
            list.setBackground(T.BG3);
            list.setForeground(T.TEXT);
            list.setFont(new Font("Consolas", Font.PLAIN, 13));
            list.setSelectionBackground(new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 70));
            list.setSelectionForeground(T.TEXT);
            list.setCellRenderer(new CompletionRenderer());
            list.setFocusable(false);

            list.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) insertSelected();
                }
            });

            JScrollPane sp = new JScrollPane(list);
            sp.setBorder(null);
            sp.getViewport().setBackground(T.BG3);
            sp.getVerticalScrollBar().setUI(new ThinScrollBarUI());
            sp.setPreferredSize(new Dimension(320, 200));

            content.add(sp, BorderLayout.CENTER);

            detailLabel = new JLabel(" ");
            detailLabel.setFont(new Font("Dialog", Font.PLAIN, 11));
            detailLabel.setForeground(T.TEXT3);
            detailLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, T.BORDER),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
            detailLabel.setBackground(T.BG4);
            detailLabel.setOpaque(true);
            content.add(detailLabel, BorderLayout.SOUTH);

            list.addListSelectionListener(e -> {
                CompletionItem sel = list.getSelectedValue();
                if (sel != null) detailLabel.setText(sel.detail);
            });

            setContentPane(content);
            setSize(340, 240);
        }

        class CompletionRenderer extends JLabel implements ListCellRenderer<CompletionItem> {
            CompletionRenderer() {
                setOpaque(true);
                setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            }
            public Component getListCellRendererComponent(JList<? extends CompletionItem> list,
                    CompletionItem item, int index, boolean isSelected, boolean cellHasFocus) {
                String icon = switch(item.type) {
                    case 0 -> "🔷"; case 1 -> "ƒ"; case 3 -> "📦"; case 4 -> "⚡"; case 5 -> "🏷️";
                    default -> "•";
                };
                setText("<html><font color='" + colorToHex(getIconColor(item.type)) + "'>" + icon + "</font>  <b>" + item.label + "</b>  <font color='#888888'>" + item.detail + "</font></html>");
                setFont(new Font("Consolas", Font.PLAIN, 13));
                if (isSelected) {
                    setBackground(new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 60));
                    setForeground(T.TEXT);
                } else {
                    setBackground(T.BG3);
                    setForeground(T.TEXT);
                }
                return this;
            }
            private Color getIconColor(int type) {
                return switch(type) {
                    case 0 -> T.C_KEYWORD; case 1 -> T.C_FUNCTION; case 3 -> T.C_CLASS;
                    case 4 -> T.PURPLE; case 5 -> T.YELLOW;
                    default -> T.TEXT;
                };
            }
            private String colorToHex(Color c) {
                return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
            }
        }

        void showAt(JTextPane ed, Point location) {
            this.editor = ed;
            setLocation(location);
            if (model.getSize() > 0) {
                list.setSelectedIndex(0);
                setVisible(true);
                requestFocus();
            }
        }

        void hideCompletion() { setVisible(false); }

        void insertSelected() {
            CompletionItem sel = list.getSelectedValue();
            if (sel != null && editor != null) {
                try {
                    Document doc = editor.getDocument();
                    String text = doc.getText(0, doc.getLength());
                    int caret = editor.getCaretPosition();
                    int start = caret;
                    while (start > 0 && Character.isJavaIdentifierPart(text.charAt(start - 1))) start--;
                    doc.remove(start, caret - start);
                    doc.insertString(start, sel.snippet, null);
                    editor.setCaretPosition(start + sel.snippet.length());
                } catch (BadLocationException ex) { ex.printStackTrace(); }
            }
            hideCompletion();
        }

        void navigateUp() {
            int idx = list.getSelectedIndex();
            if (idx > 0) list.setSelectedIndex(idx - 1);
        }
        void navigateDown() {
            int idx = list.getSelectedIndex();
            if (idx < model.getSize() - 1) list.setSelectedIndex(idx + 1);
        }

        void filter(String prefix) {
            CompletionItem[] all = model.toArray(new CompletionItem[0]);
            model.clear();
            for (CompletionItem it : all) {
                if (it.label.toLowerCase().startsWith(prefix.toLowerCase())) model.addElement(it);
            }
            if (model.getSize() == 0) hideCompletion();
            else if (list.getSelectedIndex() < 0) list.setSelectedIndex(0);
        }
    }

    private void showAutocomplete(JTextPane ed) {
        if (completionPopup == null) completionPopup = new CompletionPopup(this);
        String lang = (String) langBox.getSelectedItem();
        if (lang == null) return;
        List<CompletionItem> items = COMPLETIONS.get(lang);
        if (items == null) return;

        try {
            String text = ed.getDocument().getText(0, ed.getCaretPosition());
            int start = ed.getCaretPosition();
            while (start > 0 && Character.isJavaIdentifierPart(text.charAt(start - 1))) start--;
            String prefix = text.substring(start, ed.getCaretPosition());

            DefaultListModel<CompletionItem> model = (DefaultListModel<CompletionItem>) ((JList<?>) getPrivateField(completionPopup, "list")).getModel();
            model.clear();
            for (CompletionItem it : items) {
                if (prefix.isEmpty() || it.label.toLowerCase().startsWith(prefix.toLowerCase())) {
                    model.addElement(it);
                }
            }

            if (model.getSize() == 0) {
                completionPopup.hideCompletion();
                return;
            }

            Point caretPos = ed.modelToView2D(ed.getCaretPosition()).getBounds().getLocation();
            SwingUtilities.convertPointToScreen(caretPos, ed);
            caretPos.y += ed.getFont().getSize() + 4;
            completionPopup.showAt(ed, caretPos);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private Object getPrivateField(Object obj, String field) {
        try {
            java.lang.reflect.Field f = obj.getClass().getDeclaredField(field);
            f.setAccessible(true);
            return f.get(obj);
        } catch (Exception e) { return null; }
    }

    // ── TERMINAL PANEL ───────────────────────────────────────────────────────
    private JPanel buildTermPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 0));
        p.setBackground(T.BG2);

        JPanel tabBar = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, T.BG3, 0, getHeight(), T.BG2);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        tabBar.setOpaque(false);
        tabBar.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, T.BORDER));
        tabBar.setPreferredSize(new Dimension(0, 34));

        JPanel tabBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tabBtns.setOpaque(false);
        for (String t : new String[]{"TERMINAL","OUTPUT","PROBLEMS"}) {
            JLabel tb = new JLabel(t) {
                @Override protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (getText().equals("TERMINAL")) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setColor(T.ACCENT);
                        g2.fillRect(0, getHeight()-2, getWidth(), 2);
                        g2.dispose();
                    }
                }
            };
            tb.setFont(new Font("Dialog", Font.BOLD, 11));
            tb.setForeground(t.equals("TERMINAL") ? T.TEXT : T.TEXT3);
            tb.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
            tb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            tb.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { tb.setForeground(T.TEXT); }
                public void mouseExited(MouseEvent e)  { tb.setForeground(t.equals("TERMINAL") ? T.TEXT : T.TEXT3); }
            });
            tabBtns.add(tb);
        }
        tabBar.add(tabBtns, BorderLayout.WEST);

        JPanel tbRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 4));
        tbRight.setOpaque(false);
        JButton clrBtn = new JButton("⊘ Clear") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 30));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        clrBtn.setFont(new Font("Dialog", Font.PLAIN, 11));
        clrBtn.setForeground(T.TEXT3); clrBtn.setBackground(null);
        clrBtn.setContentAreaFilled(false); clrBtn.setBorderPainted(false); clrBtn.setFocusPainted(false);
        clrBtn.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        clrBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        clrBtn.addActionListener(e -> clearTerm());
        clrBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { clrBtn.setForeground(T.ACCENT); }
            public void mouseExited(MouseEvent e)  { clrBtn.setForeground(T.TEXT3); }
        });
        tbRight.add(clrBtn);
        tabBar.add(tbRight, BorderLayout.EAST);
        p.add(tabBar, BorderLayout.NORTH);

        terminal = new JTextPane();
        terminal.setBackground(T.BG); terminal.setForeground(T.TEXT);
        terminal.setFont(new Font("Consolas", Font.PLAIN, 13));
        terminal.setMargin(new Insets(8, 14, 8, 14));
        terminal.setEditable(false);
        termDoc = terminal.getStyledDocument();

        JScrollPane tsp = new JScrollPane(terminal);
        tsp.setBorder(null); tsp.getViewport().setBackground(T.BG);
        tsp.getVerticalScrollBar().setUI(new ThinScrollBarUI());
        p.add(tsp, BorderLayout.CENTER);

        JPanel inputRow = new JPanel(new BorderLayout(0, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(T.BG2);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(T.BORDER);
                g2.drawLine(0, 0, getWidth(), 0);
                g2.dispose();
            }
        };
        inputRow.setOpaque(false);
        inputRow.setPreferredSize(new Dimension(0, 36));

        JLabel prompt = new JLabel("  ❯ ") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, T.GRAD1, getWidth(), 0, T.GRAD2);
                g2.setPaint(gp);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), 6, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        prompt.setFont(new Font("Consolas", Font.BOLD, 14));
        prompt.setPreferredSize(new Dimension(32, 36));

        JTextField cmdInput = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        cmdInput.setBackground(T.BG2); cmdInput.setForeground(T.TEXT);
        cmdInput.setCaretColor(T.ACCENT);
        cmdInput.setFont(new Font("Consolas", Font.PLAIN, 13));
        cmdInput.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 8));
        cmdInput.addActionListener(e -> execCmd(cmdInput.getText(), cmdInput));

        inputRow.add(prompt, BorderLayout.WEST);
        inputRow.add(cmdInput, BorderLayout.CENTER);
        p.add(inputRow, BorderLayout.SOUTH);
        return p;
    }

    private void execCmd(String cmd, JTextField input) {
        if (cmd == null || cmd.isBlank()) return;
        String c = cmd.trim();
        tprint("\n  ❯ " + c + "\n", T.ACCENT);
        input.setText("");
        new SwingWorker<Void, String>() {
            protected Void doInBackground() throws Exception {
                String[] sh = Cfg.isWin() ? new String[]{"cmd","/c",c} : new String[]{"/bin/sh","-c",c};
                ProcessBuilder pb = new ProcessBuilder(sh).redirectErrorStream(true);
                if (projectRoot != null) pb.directory(projectRoot);
                Process proc = pb.start();
                try (BufferedReader r = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
                    String line;
                    while ((line = r.readLine()) != null) publish(line + "\n");
                }
                publish("\n  ─── exit " + proc.waitFor() + " ───\n");
                return null;
            }
            protected void process(List<String> chunks) { for (String s : chunks) tprint(s, T.TEXT); }
        }.execute();
    }

    // ── SYNTAX HIGHLIGHTING ──────────────────────────────────────────────────
    private void scheduleHl(JTextPane ed) {
        if (hlTimer != null) hlTimer.stop();
        hlTimer = new javax.swing.Timer(200, e -> applyHl(ed));
        hlTimer.setRepeats(false);
        hlTimer.start();
    }

    private void applyHl(JTextPane ed) {
        StyledDocument doc = ed.getStyledDocument();
        String fullText;
        try { fullText = doc.getText(0, doc.getLength()); }
        catch (BadLocationException e) { return; }

        JScrollPane sp = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, ed);
        int startPos = 0, endPos = fullText.length();
        if (sp != null) {
            Rectangle view = sp.getViewport().getViewRect();
            Point topPt    = new Point(0, Math.max(0, view.y - 100));
            Point bottomPt = new Point(0, view.y + view.height + 100);
            startPos = ed.viewToModel2D(topPt);
            endPos   = ed.viewToModel2D(bottomPt);
            startPos = Math.max(0, startPos);
            endPos   = Math.min(fullText.length(), endPos);
        }

        SimpleAttributeSet base = new SimpleAttributeSet();
        StyleConstants.setForeground(base, T.TEXT);
        doc.setCharacterAttributes(startPos, endPos - startPos, base, true);

        String lang = (String) langBox.getSelectedItem();
        if (lang == null) return;

        final int rs = startPos, re = endPos;

        switch (lang) {
            case "Java","Kotlin","C++","C","JavaScript","TypeScript","Rust","Go" -> {
                hl(doc, fullText, "/\\*[\\s\\S]*?\\*/",                   T.C_COMMENT,  rs, re);
                hl(doc, fullText, "//[^\\n]*",                            T.C_COMMENT,  rs, re);
                hl(doc, fullText, "`[^`]*`|\"[^\"]*\"|'[^']*'",          T.C_STRING,   rs, re);
                hl(doc, fullText, "\\b(0x[0-9a-fA-F]+|\\d+\\.?\\d*)\\b",T.C_NUMBER,   rs, re);
                hl(doc, fullText, "[{}()\\[\\]]",                         T.C_BRACKET,  rs, re);
            }
            case "Python" -> {
                hl(doc, fullText, "#[^\\n]*",                                                       T.C_COMMENT, rs, re);
                hl(doc, fullText, "\"\"\"[\\s\\S]*?\"\"\"|'''[\\s\\S]*?'''|\"[^\"]*\"|'[^']*'",   T.C_STRING,  rs, re);
                hl(doc, fullText, "\\b\\d+\\.?\\d*\\b",                                            T.C_NUMBER,  rs, re);
            }
            case "HTML/CSS/JS" -> {
                hl(doc, fullText, "<!--[\\s\\S]*?-->",            T.C_COMMENT,  rs, re);
                hl(doc, fullText, "\"[^\"]*\"|'[^']*'",           T.C_STRING,   rs, re);
                hl(doc, fullText, "</?[a-zA-Z][a-zA-Z0-9\\-]*", T.C_FUNCTION, rs, re);
                hl(doc, fullText, " [a-zA-Z\\-]+(?==)",           T.C_KEYWORD,  rs, re);
            }
        }

        String kw = switch (lang) {
            case "Java"   -> "\\b(public|private|protected|class|interface|enum|extends|implements|static|final|void|new|return|if|else|for|while|do|try|catch|finally|throw|throws|import|package|this|super|null|true|false|int|long|double|float|boolean|char|byte|short|String|var|record|sealed|permits)\\b";
            case "Python" -> "\\b(def|class|import|from|as|return|if|elif|else|for|while|try|except|finally|with|pass|break|continue|lambda|yield|raise|in|not|and|or|is|None|True|False|self|super|async|await)\\b";
            case "JavaScript","TypeScript" -> "\\b(const|let|var|function|class|extends|return|if|else|for|while|do|try|catch|finally|import|export|default|new|this|null|undefined|true|false|async|await|of|in|typeof|instanceof|switch|case|break|continue|throw|from|static|interface|type|enum)\\b";
            case "C","C++" -> "\\b(int|long|double|float|char|void|bool|auto|const|static|return|if|else|for|while|do|struct|class|namespace|using|include|define|typedef|sizeof|new|delete|template|typename|public|private|protected|virtual|override|nullptr|true|false)\\b";
            case "Rust"   -> "\\b(fn|let|mut|pub|use|mod|struct|enum|impl|trait|for|while|loop|if|else|match|return|self|Self|super|crate|const|static|type|where|async|await|move|ref|box|dyn|unsafe|extern|true|false|i32|i64|u32|u64|f32|f64|bool|String|Vec|Option|Result)\\b";
            case "Go"     -> "\\b(func|var|const|type|struct|interface|map|chan|go|defer|select|switch|case|default|return|if|else|for|range|break|continue|import|package|true|false|nil|make|new|append|len|cap|string|int|bool|float64|byte|error)\\b";
            default -> null;
        };
        if (kw != null) hl(doc, fullText, kw, T.C_KEYWORD, rs, re);

        hl(doc, fullText, "\\b[a-zA-Z_][a-zA-Z0-9_]*(?=\\s*\\()", T.C_FUNCTION, rs, re);
        hl(doc, fullText, "\\b[A-Z][a-zA-Z0-9]*\\b",               T.C_CLASS,   rs, re);
    }

    private void hl(StyledDocument doc, String text, String regex, Color c, int rangeStart, int rangeEnd) {
        SimpleAttributeSet a = new SimpleAttributeSet();
        StyleConstants.setForeground(a, c);
        try {
            Matcher m = cached(regex).matcher(text);
            while (m.find()) {
                int s = m.start(), e = m.end();
                if (e <= rangeStart) continue;
                if (s >= rangeEnd) break;
                int clampS = Math.max(s, rangeStart);
                int clampE = Math.min(e, rangeEnd);
                doc.setCharacterAttributes(clampS, clampE - clampS, a, false);
            }
        } catch (Exception ignored) {}
    }

    // ── LINE GUTTER ──────────────────────────────────────────────────────────
    class LineGutter extends JPanel implements DocumentListener, CaretListener {
        private final JTextComponent ed;
        LineGutter(JTextComponent ed) {
            this.ed = ed;
            setPreferredSize(new Dimension(56, 0));
            setBackground(T.BG);
            setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, T.BORDER));
            ed.getDocument().addDocumentListener(this);
            ed.addCaretListener(this);
        }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(new Font("Consolas", Font.PLAIN, 13));
            FontMetrics fm = g2.getFontMetrics();
            Rectangle clip = g.getClipBounds();
            Element root = ed.getDocument().getDefaultRootElement();
            int s = root.getElementIndex(ed.viewToModel2D(new Point(0, clip.y)));
            int e = root.getElementIndex(ed.viewToModel2D(new Point(0, clip.y + clip.height)));
            int caret = root.getElementIndex(ed.getCaretPosition());
            for (int i = s; i <= e; i++) {
                try {
                    Rectangle r = ed.modelToView2D(root.getElement(i).getStartOffset()).getBounds();
                    if (i == caret) {
                        GradientPaint gp = new GradientPaint(0, r.y, new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 30), getWidth(), r.y, new Color(T.ACCENT2.getRed(), T.ACCENT2.getGreen(), T.ACCENT2.getBlue(), 10));
                        g2.setPaint(gp);
                        g2.fillRect(0, r.y, getWidth(), r.height);
                        g2.setColor(T.ACCENT);
                    } else g2.setColor(T.TEXT3);
                    String ln = String.valueOf(i + 1);
                    g2.drawString(ln, getWidth() - fm.stringWidth(ln) - 10, r.y + r.height - fm.getDescent());
                } catch (BadLocationException ignored) {}
            }
            g2.dispose();
        }
        void upd() { SwingUtilities.invokeLater(this::repaint); }
        public void insertUpdate(DocumentEvent e) { upd(); }
        public void removeUpdate(DocumentEvent e) { upd(); }
        public void changedUpdate(DocumentEvent e) {}
        public void caretUpdate(CaretEvent e) { upd(); }
    }

    // ── STATUS BAR ───────────────────────────────────────────────────────────
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, T.GRAD1, getWidth(), 0, T.GRAD2);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        bar.setOpaque(false);
        bar.setPreferredSize(new Dimension(0, 26));
        bar.setBorder(null);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.add(statusItem("⎇  main", true));
        left.add(statusItem("✗ 0   ⚠ 0", false));
        bar.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(statusItem("UTF-8", false));
        cursorLbl = new JLabel("  Ln 1, Col 1  ");
        cursorLbl.setFont(new Font("Dialog", Font.PLAIN, 11));
        cursorLbl.setForeground(Color.WHITE);
        right.add(cursorLbl);
        right.add(statusItem("LF", false));
        bar.add(right, BorderLayout.EAST);

        statusLbl = new JLabel("  ✨ Nova IDE — Ready");
        statusLbl.setFont(new Font("Dialog", Font.BOLD, 11));
        statusLbl.setForeground(Color.WHITE);
        bar.add(statusLbl, BorderLayout.CENTER);
        return bar;
    }

    private JLabel statusItem(String t, boolean bold) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Dialog", bold ? Font.BOLD : Font.PLAIN, 11));
        l.setForeground(new Color(255,255,255,230));
        l.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return l;
    }

    private void setStatus(String t, Color c) {
        SwingUtilities.invokeLater(() -> { statusLbl.setText("  " + t); });
    }

    private void updateCursor(JTextPane ed) {
        if (cursorLbl == null) return;
        int pos = ed.getCaretPosition();
        try {
            StyledDocument doc = ed.getStyledDocument();
            int line = doc.getDefaultRootElement().getElementIndex(pos) + 1;
            int lineStart = doc.getDefaultRootElement().getElement(line-1).getStartOffset();
            cursorLbl.setText("  Ln " + line + ", Col " + (pos - lineStart + 1) + "  ");
        } catch (Exception ignored) {}
    }

    // ── THEME SWITCHING ──────────────────────────────────────────────────────
    private void applyNewTheme(String name) {
        T = THEMES.getOrDefault(name, THEMES.get("Nova Aurora"));
        Cfg.setTheme(name);
        SwingUtilities.updateComponentTreeUI(this);
        getContentPane().setBackground(T.BG);
        repaint();
        tprint("🎨  Theme → " + name + "\n", T.ACCENT);
    }

    private void cycleTheme() {
        List<String> keys = new ArrayList<>(THEMES.keySet());
        int idx = keys.indexOf(T.name);
        String next = keys.get((idx + 1) % keys.size());
        applyNewTheme(next);
        if (themeBox != null) themeBox.setSelectedItem(next);
    }

    // ── FILE OPS ─────────────────────────────────────────────────────────────
    private boolean isSafeName(String name) {
        if (name == null || name.isBlank()) return false;
        name = name.trim();
        if (name.equals(".") || name.equals("..")) return false;
        return !name.contains("/") && !name.contains("\\") && !name.contains(":") &&
               !name.contains("*") && !name.contains("?") && !name.contains("\"") &&
               !name.contains("<") && !name.contains(">") && !name.contains("|");
    }

    private void openProject() {
        JFileChooser fc = new JFileChooser(Cfg.getLastDir());
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            projectRoot = fc.getSelectedFile();
            Cfg.setLastDir(projectRoot.getAbsolutePath());
            setTitle("Nova IDE — " + projectRoot.getName());
            refreshTree();
            tprint("📁  " + projectRoot.getAbsolutePath() + "\n", T.TEXT2);
        }
    }

    private void openFile() {
        JFileChooser fc = new JFileChooser(Cfg.getLastDir());
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            openFileInEditor(fc.getSelectedFile());
            Cfg.setLastDir(fc.getSelectedFile().getParent());
        }
    }

    private void saveFile() {
        if (activeEd == null) return;
        File known = editorFiles.get(activeEd);
        if (known != null) {
            try {
                Files.writeString(known.toPath(), activeEd.getText());
                editorModified.put(activeEd, false);
                updateTabAfterSave(activeEd);
                tprint("💾  Saved → " + known.getName() + "\n", T.GREEN);
                setStatus("✨ Saved — " + known.getName(), T.GREEN);
            } catch (IOException e) { tprint("❌  " + e.getMessage() + "\n", T.RED); }
            return;
        }
        saveFileAs();
    }

    private void saveFileAs() {
        if (activeEd == null) return;
        int idx = tabs.getSelectedIndex();
        if (idx < 0) return;
        JFileChooser fc = new JFileChooser(Cfg.getLastDir());
        fc.setSelectedFile(new File(tabs.getTitleAt(idx).replace("● ", "")));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try {
                Files.writeString(f.toPath(), activeEd.getText());
                editorFiles.put(activeEd, f);
                editorModified.put(activeEd, false);
                updateTabAfterSave(activeEd);
                tprint("💾  Saved → " + f.getName() + "\n", T.GREEN);
                setStatus("✨ Saved — " + f.getName(), T.GREEN);
            } catch (IOException e) { tprint("❌  " + e.getMessage() + "\n", T.RED); }
        }
    }

    private void updateTabAfterSave(JTextPane ed) {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            Component comp = tabs.getComponentAt(i);
            if (comp instanceof JScrollPane sp && sp.getViewport().getView() == ed) {
                String title = tabs.getTitleAt(i);
                if (title.startsWith("● ")) tabs.setTitleAt(i, title.substring(2));
                comp.repaint();
                break;
            }
        }
    }

    private void openFileInEditor(File f) {
        // Check if already open
        for (int i = 0; i < tabs.getTabCount(); i++) {
            Component comp = tabs.getComponentAt(i);
            if (comp instanceof JScrollPane sp && sp.getViewport().getView() instanceof JTextPane tp) {
                File existing = editorFiles.get(tp);
                if (existing != null && existing.getAbsolutePath().equals(f.getAbsolutePath())) {
                    tabs.setSelectedIndex(i);
                    return;
                }
            }
        }
        try {
            String content = Files.readString(f.toPath());
            addNewTab(f.getName(), content, f);
            updateLangBox(f.getName());
        } catch (IOException e) { tprint("❌  " + e.getMessage() + "\n", T.RED); }
    }

    private void updateLangBox(String name) {
        Lang l = getLang(name);
        if (l != null) langBox.setSelectedItem(l.name);
    }

    private void createNewFile(File parent) {
        String name = JOptionPane.showInputDialog(this, "File name:");
        if (!isSafeName(name)) { tprint("❌ Invalid file name\n", T.RED); return; }
        try {
            File f = new File(parent, name.trim());
            if (f.createNewFile()) { refreshTree(); openFileInEditor(f); }
        } catch (IOException e) { tprint("❌  " + e.getMessage() + "\n", T.RED); }
    }

    private void createNewFolder(File parent) {
        String name = JOptionPane.showInputDialog(this, "Folder name:");
        if (!isSafeName(name)) { tprint("❌ Invalid folder name\n", T.RED); return; }
        new File(parent, name.trim()).mkdir();
        refreshTree();
    }

    private void renameFile(File f) {
        String n = JOptionPane.showInputDialog(this, "New name:", f.getName());
        if (!isSafeName(n)) { tprint("❌ Invalid name\n", T.RED); return; }
        if (f.renameTo(new File(f.getParentFile(), n.trim()))) refreshTree();
    }

    private void deleteFile(File f) {
        int ok = JOptionPane.showConfirmDialog(this, "Delete " + f.getName() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) { delRec(f); refreshTree(); }
    }

    private void delRec(File f) {
        if (f.isDirectory()) { File[] ch = f.listFiles(); if (ch != null) for (File c : ch) delRec(c); }
        f.delete();
    }

    private void pasteToDir(File dest) {
        if (!clip.has()) return;
        File target = new File(dest, clip.src.getName());
        try {
            if (clip.isCopy()) copyRec(clip.src, target);
            else clip.src.renameTo(target);
            clip.clear(); refreshTree();
        } catch (IOException e) { tprint("❌  " + e.getMessage() + "\n", T.RED); }
    }

    private void copyRec(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            dest.mkdirs();
            File[] ch = src.listFiles();
            if (ch != null) for (File c : ch) copyRec(c, new File(dest, c.getName()));
        } else Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    private void refreshTree() {
        if (projectRoot == null || !projectRoot.exists()) return;
        treeModel = new DefaultTreeModel(buildNode(projectRoot));
        fileTree.setModel(treeModel);
        expandAll(fileTree, new TreePath(treeModel.getRoot()), true);
    }

    private DefaultMutableTreeNode buildNode(File dir) {
        DefaultMutableTreeNode n = new DefaultMutableTreeNode(dir);
        File[] files = dir.listFiles();
        if (files != null) {
            Arrays.sort(files, (a, b) -> {
                if (a.isDirectory() && !b.isDirectory()) return -1;
                if (!a.isDirectory() && b.isDirectory()) return 1;
                return a.getName().compareToIgnoreCase(b.getName());
            });
            for (File f : files) {
                if (f.getName().startsWith(".")) continue;
                n.add(f.isDirectory() ? buildNode(f) : new DefaultMutableTreeNode(f));
            }
        }
        return n;
    }

    private void expandAll(JTree tree, TreePath parent, boolean expand) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent.getLastPathComponent();
        for (Enumeration<?> e = node.children(); e.hasMoreElements(); )
            expandAll(tree, parent.pathByAddingChild(e.nextElement()), expand);
        if (expand) tree.expandPath(parent);
    }

    // ── QUICK OPEN ───────────────────────────────────────────────────────────
    private void showQuickOpen() {
        if (projectRoot == null) {
            tprint("📁  Open a folder first\n", T.WARNING);
            return;
        }
        String query = JOptionPane.showInputDialog(this, "Quick Open (filename):");
        if (query == null || query.isBlank()) return;
        final String q = query.toLowerCase();
        List<File> matches = new ArrayList<>();
        try {
            Files.walk(projectRoot.toPath())
                .map(Path::toFile)
                .filter(f -> f.isFile() && !f.isHidden() && !f.getName().startsWith("."))
                .filter(f -> f.getName().toLowerCase().contains(q))
                .limit(50)
                .forEach(matches::add);
        } catch (IOException e) {}

        if (matches.isEmpty()) {
            tprint("🔍  No files match: " + query + "\n", T.WARNING);
            return;
        }

        Object[] opts = matches.stream().map(f -> projectRoot.toPath().relativize(f.toPath()).toString()).toArray();
        int r = JOptionPane.showOptionDialog(this, "Select file:", "Quick Open", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opts, opts[0]);
        if (r >= 0) openFileInEditor(matches.get(r));
    }

    private void showCommandPalette() {
        String[] cmds = {
            "New File", "Open File", "Open Folder", "Save", "Save As",
            "Quick Open (Ctrl+P)", "Find (Ctrl+F)", "Replace", "Go to Line (Ctrl+G)",
            "Run Code (Ctrl+Enter)", "Toggle Live Server", "Change Theme",
            "New Tab", "Close Tab", "Clear Terminal"
        };
        int r = JOptionPane.showOptionDialog(this, "Command Palette:", "Commands", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, cmds, cmds[0]);
        if (r < 0) return;
        switch (r) {
            case 0 -> newTab();
            case 1 -> openFile();
            case 2 -> openProject();
            case 3 -> saveFile();
            case 4 -> saveFileAs();
            case 5 -> showQuickOpen();
            case 6 -> showFind();
            case 7 -> showReplace();
            case 8 -> showGotoLine();
            case 9 -> runCode();
            case 10 -> toggleLive();
            case 11 -> cycleTheme();
            case 12 -> newTab();
            case 13 -> closeCurrentTab();
            case 14 -> clearTerm();
        }
    }

    // ── RUN CODE ─────────────────────────────────────────────────────────────
    private void runCode() {
        if (activeEd == null) { tprint("❌  No active editor\n", T.RED); return; }
        String lang = (String) langBox.getSelectedItem();
        String code = activeEd.getText();
        if (code == null || code.isBlank()) { tprint("❌  Empty file\n", T.RED); return; }
        if ("HTML/CSS/JS".equals(lang)) { toggleLive(); return; }

        tprint("\n  ── Running " + lang + " ──────────────────────\n", T.TEXT3);
        setStatus("⚡ Running " + lang + "...", T.ACCENT);

        boolean ext = runModeBox != null && runModeBox.getSelectedIndex() == 1;
        new SwingWorker<Void, String>() {
            protected Void doInBackground() throws Exception {
                File tmp = Files.createTempDirectory("nova_").toFile(); tmp.deleteOnExit();
                String[] cmd = buildCmd(lang, code, tmp);
                if (cmd == null) { publish("❌  " + lang + " runner not found\n"); return null; }
                if (ext) { buildExternal(cmd, tmp).start(); publish("  [Opened in external terminal]\n"); return null; }
                ProcessBuilder pb = new ProcessBuilder(cmd).redirectErrorStream(true);
                pb.directory(tmp);
                runProcess = pb.start();
                try (BufferedReader r = new BufferedReader(new InputStreamReader(runProcess.getInputStream()))) {
                    String line;
                    while ((line = r.readLine()) != null) publish(line + "\n");
                }
                publish("\n  ── exit " + runProcess.waitFor() + " ──────────────────────\n");
                return null;
            }
            protected void process(List<String> chunks) {
                for (String s : chunks) {
                    boolean isErr = s.toLowerCase().contains("error") || s.contains("exit 1") || s.contains("Exception");
                    tprint(s, isErr ? T.RED : T.TEXT);
                }
            }
            protected void done() {
                try { get(); setStatus("✨ Done ✓", T.GREEN); }
                catch (Exception e) { setStatus("❌ Error", T.RED); tprint("❌  " + e.getMessage() + "\n", T.RED); }
                runProcess = null;
            }
        }.execute();
    }

    private String[] buildCmd(String lang, String code, File dir) throws IOException {
        return switch (lang) {
            case "Java" -> {
                Matcher m = cached("(?:public\\s+)?class\\s+(\\w+)").matcher(code);
                String cls = (m.find() && m.group(1).matches("[a-zA-Z_][a-zA-Z0-9_]*")) ? m.group(1) : "Main";
                File src = new File(dir, cls + ".java");
                Files.writeString(src.toPath(), code);
                String jc = "javac -encoding UTF-8 \"" + src.getAbsolutePath() + "\" -d \"" + dir.getAbsolutePath() + "\" && java -cp \"" + dir.getAbsolutePath() + "\" " + cls;
                yield Cfg.isWin() ? new String[]{"cmd","/c", jc} : new String[]{"/bin/sh","-c", jc.replace("\"","'")};
            }
            case "Python" -> {
                File f = new File(dir,"main.py"); Files.writeString(f.toPath(), code);
                yield new String[]{Cfg.findCmd("python3","python"), f.getAbsolutePath()};
            }
            case "JavaScript" -> {
                File f = new File(dir,"main.js"); Files.writeString(f.toPath(), code);
                yield new String[]{Cfg.findCmd("node"), f.getAbsolutePath()};
            }
            case "C++" -> {
                File f = new File(dir,"main.cpp"); Files.writeString(f.toPath(), code);
                String out = new File(dir, Cfg.isWin()?"main.exe":"main.out").getAbsolutePath();
                String c = Cfg.findCmd("g++","clang++") + " -std=c++20 \"" + f.getAbsolutePath() + "\" -o \"" + out + "\" && \"" + out + "\"";
                yield Cfg.isWin() ? new String[]{"cmd","/c",c} : new String[]{"/bin/sh","-c",c.replace("\"","'")};
            }
            case "C" -> {
                File f = new File(dir,"main.c"); Files.writeString(f.toPath(), code);
                String out = new File(dir, Cfg.isWin()?"main.exe":"main.out").getAbsolutePath();
                String c = Cfg.findCmd("gcc","clang") + " \"" + f.getAbsolutePath() + "\" -o \"" + out + "\" && \"" + out + "\"";
                yield Cfg.isWin() ? new String[]{"cmd","/c",c} : new String[]{"/bin/sh","-c",c.replace("\"","'")};
            }
            case "Rust" -> {
                File f = new File(dir,"main.rs"); Files.writeString(f.toPath(), code);
                String out = new File(dir,"main").getAbsolutePath();
                String c = "rustc \"" + f.getAbsolutePath() + "\" -o \"" + out + (Cfg.isWin()?".exe":"") + "\" && \"" + out + (Cfg.isWin()?".exe":"") + "\"";
                yield Cfg.isWin() ? new String[]{"cmd","/c",c} : new String[]{"/bin/sh","-c",c.replace("\"","'")};
            }
            case "Go" -> {
                File f = new File(dir,"main.go"); Files.writeString(f.toPath(), code);
                yield new String[]{"go","run", f.getAbsolutePath()};
            }
            default -> null;
        };
    }

    private ProcessBuilder buildExternal(String[] cmd, File dir) {
        String c = String.join(" ", cmd);
        if (Cfg.isWin()) {
            return Cfg.cmdExists("wt")
                ? new ProcessBuilder("wt","new-tab","--title","Nova Run","cmd","/c",c+" & pause")
                : new ProcessBuilder("cmd","/c","start","cmd","/k",c+" & pause");
        } else {
            if (Cfg.cmdExists("gnome-terminal")) return new ProcessBuilder("gnome-terminal","--","/bin/sh","-c",c+"; read");
            if (Cfg.cmdExists("xterm"))           return new ProcessBuilder("xterm","-e","/bin/sh","-c",c+"; read");
            return new ProcessBuilder("/bin/sh","-c",c);
        }
    }

    private void stopProcess() {
        if (runProcess != null && runProcess.isAlive()) {
            runProcess.destroyForcibly();
            tprint("\n  ⏹  Stopped.\n", T.YELLOW);
            setStatus("⏹ Stopped", T.YELLOW);
        }
    }

    // ── LIVE SERVER ──────────────────────────────────────────────────────────
    private void toggleLive() {
        if (liveOn) { stopLive(); return; }
        try {
            int port = Cfg.getLivePort();
            Path root = Files.createTempDirectory("nova_live_");
            Files.writeString(root.resolve("index.html"), injectReload(activeEd.getText()));

            liveServer = HttpServer.create(new InetSocketAddress("127.0.0.1", port), 0);
            liveServer.createContext("/", ex -> {
                String path = ex.getRequestURI().getPath().equals("/") ? "/index.html" : ex.getRequestURI().getPath();
                Path requested = root.resolve(path.substring(1)).normalize();
                if (!requested.startsWith(root)) {
                    byte[] b = "403 Forbidden".getBytes();
                    ex.sendResponseHeaders(403, b.length);
                    ex.getResponseBody().write(b); ex.getResponseBody().close(); return;
                }
                File f = requested.toFile();
                ex.getResponseHeaders().add("Cache-Control", "no-cache");
                ex.getResponseHeaders().add("Access-Control-Allow-Origin", "http://127.0.0.1:" + port);
                if (!f.exists() || f.isDirectory()) {
                    byte[] b = "404".getBytes(); ex.sendResponseHeaders(404, b.length);
                    ex.getResponseBody().write(b); ex.getResponseBody().close(); return;
                }
                byte[] body = Files.readAllBytes(f.toPath());
                if (f.getName().endsWith(".html"))
                    body = injectReload(new String(body, StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8);
                ex.getResponseHeaders().add("Content-Type", mime(f.getName()));
                ex.sendResponseHeaders(200, body.length);
                ex.getResponseBody().write(body); ex.getResponseBody().close();
            });
            liveServer.setExecutor(Executors.newCachedThreadPool());
            liveServer.start();
            liveOn = true;

            String url = "http://127.0.0.1:" + port;
            if (liveLbl != null) {
                liveLbl.setText("  ⬡ LIVE :" + port + "  ");
                liveLbl.setForeground(T.GREEN);
            }
            tprint("⬡  Live Server → " + url + "\n", T.CYAN);
            if (Desktop.isDesktopSupported()) Desktop.getDesktop().browse(new URI(url));

            Thread watcher = new Thread(() -> {
                long last = 0;
                while (liveOn) {
                    try {
                        Thread.sleep(400);
                        if (activeEd != null) {
                            long cur = activeEd.getText().hashCode();
                            if (cur != last) {
                                Files.writeString(root.resolve("index.html"), injectReload(activeEd.getText()));
                                last = cur;
                            }
                        }
                    } catch (Exception ignored) {}
                }
            }, "LiveWatcher");
            watcher.setDaemon(true);
            watcher.start();
        } catch (Exception e) { tprint("❌  " + e.getMessage() + "\n", T.RED); }
    }

    private void stopLive() {
        if (liveServer != null) liveServer.stop(0);
        liveOn = false;
        if (liveLbl != null) {
            liveLbl.setText("  ⬡ LIVE OFF  ");
            liveLbl.setForeground(T.TEXT3);
        }
        tprint("⏹  Live Server stopped.\n", T.YELLOW);
    }

    private String injectReload(String html) {
        String s = "<script>setInterval(async()=>{try{const r=await fetch(location.href,{cache:'no-store'});const t=await r.text();if(window._t&&window._t!==t)location.reload();window._t=t;}catch(e){}},800);</script>";
        return html.contains("</body>") ? html.replace("</body>", s + "</body>") : html + s;
    }

    private String mime(String n) {
        if (n.endsWith(".html")||n.endsWith(".htm")) return "text/html;charset=utf-8";
        if (n.endsWith(".css"))  return "text/css";
        if (n.endsWith(".js"))   return "application/javascript";
        if (n.endsWith(".json")) return "application/json";
        if (n.endsWith(".png"))  return "image/png";
        if (n.endsWith(".jpg")||n.endsWith(".jpeg")) return "image/jpeg";
        if (n.endsWith(".svg"))  return "image/svg+xml";
        return "text/plain";
    }

    // ── TERMINAL HELPERS ─────────────────────────────────────────────────────
    private void tprint(String text, Color c) {
        SwingUtilities.invokeLater(() -> {
            SimpleAttributeSet a = new SimpleAttributeSet();
            StyleConstants.setForeground(a, c);
            StyleConstants.setFontFamily(a, "Consolas");
            StyleConstants.setFontSize(a, 13);
            try {
                termDoc.insertString(termDoc.getLength(), text, a);
                terminal.setCaretPosition(termDoc.getLength());
            } catch (BadLocationException ignored) {}
        });
    }

    private void clearTerm() {
        SwingUtilities.invokeLater(() -> {
            try { termDoc.remove(0, termDoc.getLength()); } catch (BadLocationException ignored) {}
        });
    }

    // ── WELCOME / TOOLS CHECK ────────────────────────────────────────────────
    private void printWelcome() {
        tprint("  ✨ Nova IDE v2.0  —  Ready\n",                            T.ACCENT);
        tprint("  ─────────────────────────────────────────\n",             T.TEXT3);
        tprint("  Ctrl+Enter   Run code\n",                                 T.TEXT2);
        tprint("  Ctrl+S       Save file\n",                                T.TEXT2);
        tprint("  Ctrl+P       Quick Open\n",                               T.TEXT2);
        tprint("  Ctrl+Space   Autocomplete\n",                             T.TEXT2);
        tprint("  Ctrl+F       Find\n",                                      T.TEXT2);
        tprint("  Ctrl+G       Go to Line\n",                               T.TEXT2);
        tprint("  Ctrl+N       New Tab\n",                                  T.TEXT2);
        tprint("  Ctrl+W       Close Tab\n",                                T.TEXT2);
        tprint("  Ctrl+Shift+P Command Palette\n",                          T.TEXT2);
        tprint("  ─────────────────────────────────────────\n\n",           T.TEXT3);
    }

    private void checkTools() {
        for (String t : new String[]{"javac","python","python3","node","g++","rustc","go"})
            if (!Cfg.cmdExists(t))
                SwingUtilities.invokeLater(() -> tprint("  ⚠  " + t + " not found in PATH\n", T.YELLOW));
    }

    // ── SHORTCUTS ────────────────────────────────────────────────────────────
    private void setupShortcuts() {
        JRootPane r = getRootPane();
        bind(r, KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK, "run",      e -> runCode());
        bind(r, KeyEvent.VK_S,     InputEvent.CTRL_DOWN_MASK, "save",     e -> saveFile());
        bind(r, KeyEvent.VK_F,     InputEvent.CTRL_DOWN_MASK, "find",     e -> showFind());
        bind(r, KeyEvent.VK_N,     InputEvent.CTRL_DOWN_MASK, "newtab",   e -> newTab());
        bind(r, KeyEvent.VK_P,     InputEvent.CTRL_DOWN_MASK, "quickopen", e -> showQuickOpen());
        bind(r, KeyEvent.VK_P,     InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK, "cmdpalette", e -> showCommandPalette());
        bind(r, KeyEvent.VK_G,     InputEvent.CTRL_DOWN_MASK, "gotoline", e -> showGotoLine());
        bind(r, KeyEvent.VK_W,     InputEvent.CTRL_DOWN_MASK, "closetab", e -> closeCurrentTab());
        bind(r, KeyEvent.VK_SPACE, InputEvent.CTRL_DOWN_MASK, "autocomplete", e -> { if (activeEd != null) showAutocomplete(activeEd); });
    }

    private void bind(JRootPane r, int key, int mod, String name, ActionListener a) {
        r.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key, mod), name);
        r.getActionMap().put(name, new AbstractAction() { public void actionPerformed(ActionEvent e) { a.actionPerformed(e); } });
    }

    private void showFind() {
        if (activeEd == null) return;
        String q = JOptionPane.showInputDialog(this, "Find:");
        if (q == null || q.isEmpty()) return;
        String t = activeEd.getText();
        int i = t.indexOf(q, activeEd.getCaretPosition());
        if (i < 0) i = t.indexOf(q);
        if (i >= 0) { activeEd.setCaretPosition(i); activeEd.moveCaretPosition(i + q.length()); }
        else tprint("  Not found: " + q + "\n", T.YELLOW);
    }

    private void showReplace() {
        if (activeEd == null) return;
        String find = JOptionPane.showInputDialog(this, "Find:");
        if (find == null || find.isEmpty()) return;
        String replace = JOptionPane.showInputDialog(this, "Replace with:");
        if (replace == null) return;
        String t = activeEd.getText();
        activeEd.setText(t.replace(find, replace));
        tprint("✨ Replaced all occurrences\n", T.GREEN);
    }

    private void showGotoLine() {
        if (activeEd == null) return;
        String ln = JOptionPane.showInputDialog(this, "Go to line:");
        if (ln == null || ln.isBlank()) return;
        try {
            int line = Integer.parseInt(ln.trim()) - 1;
            StyledDocument doc = activeEd.getStyledDocument();
            Element root = doc.getDefaultRootElement();
            if (line >= 0 && line < root.getElementCount()) {
                int pos = root.getElement(line).getStartOffset();
                activeEd.setCaretPosition(pos);
            }
        } catch (Exception e) { tprint("❌ Invalid line\n", T.RED); }
    }

    // ── MAIN ─────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(NovaIDE::new);
    }
}