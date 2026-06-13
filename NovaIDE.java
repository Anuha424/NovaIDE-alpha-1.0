import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.border.*;
import javax.swing.tree.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.prefs.Preferences;
import java.util.regex.*;
import java.nio.charset.StandardCharsets;
import com.sun.net.httpserver.*;

public class NovaIDE extends JFrame {

    // ── PALETTES ────────────────────────────────────────────────────────────
    public static class Theme {
        public String name;
        public Color BG, BG2, BG3, BG4, BORDER, BORDER2;
        public Color ACCENT, ACCENT2, ACCENT3;
        public Color TEXT, TEXT2, TEXT3;
        public Color RED, YELLOW, GREEN, CYAN, PURPLE;
        public Color C_KEYWORD, C_STRING, C_COMMENT, C_NUMBER, C_FUNCTION, C_CLASS, C_BRACKET;
        public Color SCROLLBAR, SELECTION, LINE_HIGHLIGHT;

        public Theme(String n,
                     Color bg, Color bg2, Color bg3, Color bg4,
                     Color border, Color border2,
                     Color acc, Color acc2, Color acc3,
                     Color text, Color text2, Color text3,
                     Color red, Color yellow, Color green, Color cyan, Color purple,
                     Color kw, Color str, Color cmt, Color num, Color fn, Color cls, Color brk,
                     Color scroll, Color sel, Color lineHl) {
            name = n;
            BG = bg; BG2 = bg2; BG3 = bg3; BG4 = bg4;
            BORDER = border; BORDER2 = border2;
            ACCENT = acc; ACCENT2 = acc2; ACCENT3 = acc3;
            TEXT = text; TEXT2 = text2; TEXT3 = text3;
            RED = red; YELLOW = yellow; GREEN = green; CYAN = cyan; PURPLE = purple;
            C_KEYWORD = kw; C_STRING = str; C_COMMENT = cmt;
            C_NUMBER = num; C_FUNCTION = fn; C_CLASS = cls; C_BRACKET = brk;
            SCROLLBAR = scroll; SELECTION = sel; LINE_HIGHLIGHT = lineHl;
        }
    }

    public static Theme T;

    public static final Map<String, Theme> THEMES = new LinkedHashMap<>();
    static {
        // ── Nova Dark ──────────────────────────────────────────────────────
        THEMES.put("Nova Dark", new Theme("Nova Dark",
            new Color(0x1E1E2E), new Color(0x252537), new Color(0x2D2D44), new Color(0x1A1A2B),
            new Color(0x3A3A55), new Color(0x4A4A6A),
            new Color(0x7C6AF7), new Color(0x9D8BFF), new Color(0x5A48D8),
            new Color(0xCDD6F4), new Color(0xA6ADC8), new Color(0x6C7086),
            new Color(0xF38BA8), new Color(0xF9E2AF), new Color(0xA6E3A1), new Color(0x89DCEB), new Color(0xCBA6F7),
            new Color(0xCBA6F7), new Color(0xA6E3A1), new Color(0x585B70), new Color(0xFAB387), new Color(0x89B4FA), new Color(0xF9E2AF), new Color(0x89DCEB),
            new Color(0x3A3A55), new Color(0x7C6AF7, true).brighter(), new Color(0x2A2A3E)
        ));

        // ── Nova Light ──────────────────────────────────────────────────────
        THEMES.put("Nova Light", new Theme("Nova Light",
            new Color(0xFBFBFF), new Color(0xF3F3F8), new Color(0xE8E8F0), new Color(0xEAEAF5),
            new Color(0xD0D0E0), new Color(0xBBBBCC),
            new Color(0x6C53D9), new Color(0x8A72EF), new Color(0x4C38B8),
            new Color(0x1E1E30), new Color(0x4A4A60), new Color(0x8888A0),
            new Color(0xCC2222), new Color(0x996600), new Color(0x207820), new Color(0x006699), new Color(0x8844CC),
            new Color(0x8844CC), new Color(0x207820), new Color(0x888888), new Color(0x996600), new Color(0x0055CC), new Color(0xAA5500), new Color(0x006699),
            new Color(0xCCCCDD), new Color(0xD0C8FF), new Color(0xEEEEFF)
        ));

        // ── Nova Midnight ──────────────────────────────────────────────────
        THEMES.put("Nova Midnight", new Theme("Nova Midnight",
            new Color(0x0D0D14), new Color(0x12121C), new Color(0x18182A), new Color(0x0A0A10),
            new Color(0x252535), new Color(0x353550),
            new Color(0x7C6AF7), new Color(0x9D8BFF), new Color(0x5A48D8),
            new Color(0xCDD6F4), new Color(0x9098BB), new Color(0x555577),
            new Color(0xFF6B8A), new Color(0xFFD580), new Color(0x78E89A), new Color(0x61D8F0), new Color(0xC99EFF),
            new Color(0xC99EFF), new Color(0x78E89A), new Color(0x444466), new Color(0xFFB380), new Color(0x7EB4FF), new Color(0xFFD580), new Color(0x61D8F0),
            new Color(0x252535), new Color(0x3A2D8A), new Color(0x1A1A2C)
        ));
    }

    // ── SETTINGS ────────────────────────────────────────────────────────────
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
        static String getTheme() { return p.get("theme", "Nova Dark"); }
        static void setTheme(String t) { p.put("theme", t); }
    }

    // ── LANGUAGES ───────────────────────────────────────────────────────────
    static final class Lang {
        final String name; final String[] exts; final String icon;
        Lang(String n, String[] e, String i) { name=n; exts=e; icon=i; }
        boolean matches(String f) { for (String ex : exts) if (f.endsWith(ex)) return true; return false; }
    }

    static final Map<String, Lang> LANGS = new LinkedHashMap<>();
    static {
        LANGS.put("Java",       new Lang("Java",       new String[]{".java"},             "☕"));
        LANGS.put("Python",     new Lang("Python",     new String[]{".py",".pyw"},        "🐍"));
        LANGS.put("JavaScript", new Lang("JavaScript", new String[]{".js",".mjs"},        "⚡"));
        LANGS.put("TypeScript", new Lang("TypeScript", new String[]{".ts",".tsx"},        "📘"));
        LANGS.put("C",          new Lang("C",          new String[]{".c"},                "🔷"));
        LANGS.put("C++",        new Lang("C++",        new String[]{".cpp",".cc",".cxx"}, "🔷"));
        LANGS.put("Rust",       new Lang("Rust",       new String[]{".rs"},               "🦀"));
        LANGS.put("Go",         new Lang("Go",         new String[]{".go"},               "🐹"));
        LANGS.put("Kotlin",     new Lang("Kotlin",     new String[]{".kt"},               "🎯"));
        LANGS.put("Ruby",       new Lang("Ruby",       new String[]{".rb"},               "💎"));
        LANGS.put("PHP",        new Lang("PHP",        new String[]{".php"},              "🐘"));
        LANGS.put("Bash",       new Lang("Bash",       new String[]{".sh",".bash"},       "📟"));
        LANGS.put("Lua",        new Lang("Lua",        new String[]{".lua"},              "🌙"));
        LANGS.put("HTML/CSS/JS",new Lang("HTML/CSS/JS",new String[]{".html",".css",".htm"},"🌐"));
    }

    static Lang getLang(String f) {
        for (Lang l : LANGS.values()) if (l.matches(f)) return l;
        return LANGS.get("HTML/CSS/JS");
    }

    static final Map<String, String> TEMPLATES = new LinkedHashMap<>();
    static {
        TEMPLATES.put("Java",       "public class Main {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, Nova!\");\n    }\n}\n");
        TEMPLATES.put("Python",     "def main():\n    print(\"Hello, Nova!\")\n\nif __name__ == \"__main__\":\n    main()\n");
        TEMPLATES.put("JavaScript", "const greet = (name) => `Hello, ${name}!`;\nconsole.log(greet('Nova'));\n");
        TEMPLATES.put("C++",        "#include <iostream>\n\nint main() {\n    std::cout << \"Hello, Nova!\" << std::endl;\n    return 0;\n}\n");
        TEMPLATES.put("Rust",       "fn main() {\n    println!(\"Hello, Nova!\");\n}\n");
        TEMPLATES.put("Go",         "package main\n\nimport \"fmt\"\n\nfunc main() {\n    fmt.Println(\"Hello, Nova!\")\n}\n");
        TEMPLATES.put("HTML/CSS/JS","<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n    <meta charset=\"UTF-8\">\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n    <title>Nova</title>\n    <style>\n        body { font-family: sans-serif; background: #1e1e2e; color: #cdd6f4; }\n    </style>\n</head>\n<body>\n    <h1>Hello, Nova!</h1>\n</body>\n</html>\n");
    }

    // ── STATE ────────────────────────────────────────────────────────────────
    private JTabbedPane tabs;
    private JTextPane terminal;
    private StyledDocument termDoc;
    private JLabel statusLbl, liveLbl, cursorLbl, langLbl;
    private JComboBox<String> langBox, runModeBox, themeBox;
    private Process runProcess;
    private HttpServer liveServer;
    private boolean liveOn = false;
    private javax.swing.Timer hlTimer;
    private JTree fileTree;
    private DefaultTreeModel treeModel;
    private File projectRoot;
    private JTextPane activeEd;

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

    // ── CONSTRUCTOR ─────────────────────────────────────────────────────────
    public NovaIDE() {
        super("Nova IDE");
        T = THEMES.getOrDefault(Cfg.getTheme(), THEMES.get("Nova Dark"));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1520, 920);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setUndecorated(false);

        buildUI();
        setupShortcuts();
        setVisible(true);
        printWelcome();
        new Thread(() -> checkTools()).start();
    }

    // ── UI ASSEMBLY ──────────────────────────────────────────────────────────
    private void buildUI() {
        getContentPane().setBackground(T.BG);
        setLayout(new BorderLayout(0, 0));

        add(buildActivityBar(), BorderLayout.WEST);
        add(buildTitleBar(), BorderLayout.NORTH);
        JSplitPane main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildSidebar(), buildWorkArea());
        main.setResizeWeight(0.18);
        main.setDividerSize(1);
        main.setBackground(T.BORDER);
        main.setBorder(null);
        add(main, BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
    }

    // ── ACTIVITY BAR ────────────────────────────────────────────────────────
    private JPanel buildActivityBar() {
        JPanel bar = new JPanel();
        bar.setLayout(new BoxLayout(bar, BoxLayout.Y_AXIS));
        bar.setBackground(T.BG4);
        bar.setPreferredSize(new Dimension(48, 0));
        bar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, T.BORDER));

        String[][] items = {{"⬡", "Explorer"}, {"⌕", "Search"}, {"⎇", "Git"}, {"⬢", "Debug"}, {"⊞", "Extensions"}};
        for (String[] it : items) {
            bar.add(Box.createVerticalStrut(4));
            bar.add(activityBtn(it[0], it[1]));
        }
        bar.add(Box.createVerticalGlue());
        bar.add(activityBtn("⚙", "Settings"));
        bar.add(Box.createVerticalStrut(8));
        return bar;
    }

    private JButton activityBtn(String icon, String tooltip) {
        JButton b = new JButton(icon) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(T.BG3);
                    g2.fillRoundRect(6, 4, getWidth()-12, getHeight()-8, 6, 6);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Dialog", Font.PLAIN, 18));
        b.setForeground(T.TEXT3);
        b.setBackground(null);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(48, 48));
        b.setMaximumSize(new Dimension(48, 48));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setToolTipText(tooltip);
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setForeground(T.TEXT); b.repaint(); }
            public void mouseExited(MouseEvent e)  { b.setForeground(T.TEXT3); b.repaint(); }
        });
        return b;
    }

    // ── TITLE / TOOLBAR ─────────────────────────────────────────────────────
    private JPanel buildTitleBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(T.BG4);
        bar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, T.BORDER));
        bar.setPreferredSize(new Dimension(0, 78));

        JPanel menuRow = new JPanel(new BorderLayout());
        menuRow.setBackground(T.BG4);
        menuRow.setBorder(BorderFactory.createEmptyBorder(4, 56, 0, 8));

        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        brand.setBackground(T.BG4);

        JLabel logo = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, T.ACCENT, getWidth(), getHeight(), T.ACCENT2);
                g2.setPaint(gp);
                int[] xs = {10,20,20,10,0,0};
                int[] ys = {0,5,15,20,15,5};
                g2.fillPolygon(xs, ys, 6);
                g2.setColor(T.BG4);
                g2.setFont(new Font("Monospaced", Font.BOLD, 9));
                g2.drawString("N", 5, 14);
                g2.dispose();
            }
        };
        logo.setPreferredSize(new Dimension(24, 22));
        brand.add(logo);

        JLabel name = new JLabel("Nova IDE");
        name.setFont(new Font("Dialog", Font.BOLD, 13));
        name.setForeground(T.TEXT2);
        brand.add(name);

        String[] menus = {"File", "Edit", "View", "Go", "Run", "Terminal", "Help"};
        for (String m : menus) {
            JLabel ml = new JLabel(m);
            ml.setFont(new Font("Dialog", Font.PLAIN, 12));
            ml.setForeground(T.TEXT2);
            ml.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
            ml.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            ml.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { ml.setForeground(T.TEXT); }
                public void mouseExited(MouseEvent e) { ml.setForeground(T.TEXT2); }
            });
            brand.add(ml);
        }
        menuRow.add(brand, BorderLayout.WEST);

        JPanel searchWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        searchWrap.setBackground(T.BG4);
        JTextField search = new JTextField("Nova IDE") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(T.BG3);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        search.setFont(new Font("Dialog", Font.PLAIN, 12));
        search.setForeground(T.TEXT3);
        search.setBackground(new Color(0,0,0,0));
        search.setOpaque(false);
        search.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        search.setPreferredSize(new Dimension(320, 26));
        search.setHorizontalAlignment(SwingConstants.CENTER);
        searchWrap.add(search);
        menuRow.add(searchWrap, BorderLayout.CENTER);

        JPanel winBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));
        winBtns.setBackground(T.BG4);
        for (Object[] wb : new Object[][]{{"─", T.TEXT3, false}, {"□", T.TEXT3, false}, {"✕", T.RED, true}}) {
            JButton w = winBtn((String)wb[0], (Color)wb[1], (boolean)wb[2]);
            winBtns.add(w);
        }
        menuRow.add(winBtns, BorderLayout.EAST);
        bar.add(menuRow, BorderLayout.NORTH);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
        toolbar.setBackground(T.BG2);
        toolbar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, T.BORDER));
        toolbar.setPreferredSize(new Dimension(0, 40));

        langBox = new JComboBox<>(LANGS.keySet().toArray(new String[0]));
        styleCombo(langBox, 148);
        toolbar.add(langBox);

        runModeBox = new JComboBox<>(new String[]{"Terminal (Internal)", "Terminal (External)"});
        styleCombo(runModeBox, 175);
        toolbar.add(runModeBox);

        themeBox = new JComboBox<>(THEMES.keySet().toArray(new String[0]));
        themeBox.setSelectedItem(Cfg.getTheme());
        styleCombo(themeBox, 132);
        themeBox.addActionListener(e -> applyNewTheme((String) themeBox.getSelectedItem()));
        toolbar.add(themeBox);

        toolbar.add(Box.createHorizontalStrut(6));
        toolbar.add(tbSep());

        toolbar.add(toolBtn("▶ Run", T.GREEN, "Ctrl+Enter", e -> runCode()));
        toolbar.add(toolBtn("⏹ Stop", T.RED, "Stop", e -> stopProcess()));
        toolbar.add(tbSep());
        toolbar.add(toolBtn("⬡ Live", T.CYAN, "Live Server", e -> toggleLive()));
        toolbar.add(tbSep());
        toolbar.add(toolBtn("📂 Open File", T.TEXT2, null, e -> openFile()));
        toolbar.add(toolBtn("📁 Open Folder", T.TEXT2, null, e -> openProject()));
        toolbar.add(toolBtn("💾 Save", T.TEXT2, "Ctrl+S", e -> saveFile()));
        toolbar.add(tbSep());
        toolbar.add(toolBtn("⊕ New Tab", T.TEXT2, null, e -> newTab()));

        liveLbl = new JLabel("  ⬡ LIVE OFF  ");
        liveLbl.setFont(new Font("Monospaced", Font.PLAIN, 11));
        liveLbl.setForeground(T.TEXT3);
        toolbar.add(liveLbl);

        bar.add(toolbar, BorderLayout.CENTER);
        return bar;
    }

    private JButton winBtn(String icon, Color hover, boolean danger) {
        JButton b = new JButton(icon) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                if (getModel().isRollover()) {
                    g2.setColor(danger ? T.RED : T.BG3);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    if (danger) setForeground(Color.WHITE); else setForeground(T.TEXT);
                } else setForeground(T.TEXT3);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Dialog", Font.PLAIN, 12));
        b.setBackground(null);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(46, 30));
        return b;
    }

    private JSeparator tbSep() {
        JSeparator s = new JSeparator(SwingConstants.VERTICAL);
        s.setForeground(T.BORDER);
        s.setPreferredSize(new Dimension(1, 20));
        return s;
    }

    private void styleCombo(JComboBox<String> cb, int w) {
        cb.setBackground(T.BG3);
        cb.setForeground(T.TEXT2);
        cb.setFont(new Font("Monospaced", Font.PLAIN, 11));
        cb.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(T.BORDER),
            BorderFactory.createEmptyBorder(0, 4, 0, 4)));
        cb.setPreferredSize(new Dimension(w, 26));
    }

    private JButton toolBtn(String text, Color fg, String tooltip, ActionListener a) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(T.BG3);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                }
                if (getModel().isPressed()) {
                    g2.setColor(T.BG4);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Dialog", Font.PLAIN, 12));
        b.setForeground(fg);
        b.setBackground(null);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        if (tooltip != null) b.setToolTipText(tooltip);
        b.addActionListener(a);
        return b;
    }

    // ── SIDEBAR ──────────────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(T.BG2);
        panel.setPreferredSize(new Dimension(260, 0));
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, T.BORDER));

        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBackground(T.BG2);
        hdr.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 10));
        JLabel lbl = new JLabel("EXPLORER");
        lbl.setFont(new Font("Dialog", Font.BOLD, 10));
        lbl.setForeground(T.TEXT3);
        hdr.add(lbl, BorderLayout.WEST);

        JPanel hdrBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        hdrBtns.setBackground(T.BG2);
        for (String[] ic : new String[][]{{"⊕","New File"},{"⊞","New Folder"},{"↻","Refresh"}}) {
            JLabel btn = new JLabel(ic[0]);
            btn.setFont(new Font("Dialog", Font.PLAIN, 14));
            btn.setForeground(T.TEXT3);
            btn.setToolTipText(ic[1]);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { btn.setForeground(T.TEXT); }
                public void mouseExited(MouseEvent e) { btn.setForeground(T.TEXT3); }
                public void mouseClicked(MouseEvent e) {
                    if (ic[1].equals("New File")) { if (projectRoot!=null) createNewFile(projectRoot); }
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
        fileTree.setRowHeight(24);
        fileTree.setCellRenderer(new NovaTreeRenderer());
        fileTree.setShowsRootHandles(true);
        fileTree.addMouseListener(new TreeCtxListener());
        fileTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
            if (node != null && node.getUserObject() instanceof File) {
                File f = (File) node.getUserObject();
                if (f.isFile()) openFileInEditor(f);
            }
        });

        JScrollPane tsp = new JScrollPane(fileTree);
        tsp.setBorder(null);
        tsp.getViewport().setBackground(T.BG2);
        tsp.getVerticalScrollBar().setUI(new ThinScrollBarUI());
        panel.add(tsp, BorderLayout.CENTER);
        return panel;
    }

    // Thin modern scrollbar
    static class ThinScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override protected void configureScrollBarColors() {
            thumbColor = new Color(T.SCROLLBAR.getRed(), T.SCROLLBAR.getGreen(), T.SCROLLBAR.getBlue(), 180);
            trackColor = new Color(0,0,0,0);
        }
        @Override protected JButton createDecreaseButton(int o) { JButton b = new JButton(); b.setPreferredSize(new Dimension(0,0)); return b; }
        @Override protected JButton createIncreaseButton(int o) { JButton b = new JButton(); b.setPreferredSize(new Dimension(0,0)); return b; }
        @Override protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(r.x+2, r.y, r.width-4, r.height, 4, 4);
            g2.dispose();
        }
        @Override protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            g.setColor(trackColor); g.fillRect(r.x, r.y, r.width, r.height);
        }
    }

    // ── VS Code-style Tree Renderer with Vector Icons ───────────────────────
    class NovaTreeRenderer extends DefaultTreeCellRenderer {
        @Override public Component getTreeCellRendererComponent(
                JTree tree, Object val, boolean sel, boolean exp, boolean leaf, int row, boolean focus) {
            super.getTreeCellRendererComponent(tree, val, sel, exp, leaf, row, focus);
            setOpaque(true);
            setBackgroundSelectionColor(new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 40));
            setBackgroundNonSelectionColor(T.BG2);
            setBorderSelectionColor(new Color(0,0,0,0));
            setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
            setFont(new Font("Dialog", Font.PLAIN, 13));

            if (val instanceof DefaultMutableTreeNode) {
                Object uo = ((DefaultMutableTreeNode)val).getUserObject();
                if (uo instanceof File) {
                    File f = (File)uo;
                    if (f.isDirectory()) {
                        setIcon(createFolderIcon(exp));
                        setForeground(sel ? T.TEXT : T.TEXT2);
                    } else {
                        setIcon(createFileIcon(f.getName()));
                        setForeground(T.TEXT);
                    }
                    setText(f.getName());
                } else {
                    setText("  " + uo.toString());
                    setForeground(T.TEXT3);
                }
            }
            if (sel) {
                setBackground(new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 40));
            } else {
                setBackground(new Color(0,0,0,0));
            }
            return this;
        }

        private ImageIcon createFolderIcon(boolean open) {
            BufferedImage img = new BufferedImage(18, 18, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color c = open ? new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 220) : T.YELLOW;
            g2.setColor(c);
            g2.fillRoundRect(1, 5, 16, 11, 2, 2);
            g2.fillRect(3, 3, 7, 4);
            if (open) {
                g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 160));
                g2.fillRoundRect(1, 8, 16, 8, 2, 2);
            }
            g2.dispose();
            return new ImageIcon(img);
        }

        private ImageIcon createFileIcon(String name) {
            BufferedImage img = new BufferedImage(18, 18, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color c = getFileColor(name);
            g2.setColor(c);
            g2.setStroke(new BasicStroke(1.3f));
            // Document shape
            int[] xs = {4, 11, 14, 14, 4};
            int[] ys = {2, 2, 5, 16, 16};
            g2.fillPolygon(xs, ys, 5);
            // Fold
            g2.setColor(new Color(0,0,0,40));
            int[] fx = {11, 14, 14, 11};
            int[] fy = {2, 5, 5, 5};
            g2.fillPolygon(fx, fy, 4);
            // Accent line
            g2.setColor(new Color(255,255,255,80));
            g2.drawLine(6, 9, 12, 9);
            g2.drawLine(6, 12, 12, 12);
            g2.dispose();
            return new ImageIcon(img);
        }

        private Color getFileColor(String name) {
            String n = name.toLowerCase();
            if (n.endsWith(".java"))  return new Color(255, 152, 0);
            if (n.endsWith(".py") || n.endsWith(".pyw")) return new Color(53, 114, 165);
            if (n.endsWith(".js") || n.endsWith(".mjs")) return new Color(240, 210, 80);
            if (n.endsWith(".ts") || n.endsWith(".tsx")) return new Color(49, 120, 198);
            if (n.endsWith(".html") || n.endsWith(".htm")) return new Color(227, 76, 38);
            if (n.endsWith(".css"))  return new Color(21, 114, 187);
            if (n.endsWith(".json")) return new Color(255, 206, 102);
            if (n.endsWith(".md"))   return new Color(100, 140, 180);
            if (n.endsWith(".rs"))   return new Color(220, 100, 60);
            if (n.endsWith(".go"))   return new Color(0, 173, 216);
            if (n.endsWith(".cpp") || n.endsWith(".cc") || n.endsWith(".cxx") || n.endsWith(".c") || n.endsWith(".h")) return new Color(0, 89, 155);
            if (n.endsWith(".kt"))   return new Color(160, 120, 220);
            if (n.endsWith(".rb"))   return new Color(200, 60, 60);
            if (n.endsWith(".php"))  return new Color(120, 130, 200);
            if (n.endsWith(".sh") || n.endsWith(".bash")) return new Color(100, 160, 100);
            if (n.endsWith(".lua"))  return new Color(0, 80, 180);
            if (n.endsWith(".xml"))  return new Color(180, 100, 60);
            if (n.endsWith(".yaml") || n.endsWith(".yml")) return new Color(200, 80, 80);
            if (n.endsWith(".png") || n.endsWith(".jpg") || n.endsWith(".jpeg") || n.endsWith(".svg")) return new Color(160, 100, 180);
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
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)fileTree.getLastSelectedPathComponent();
            if (node==null || !(node.getUserObject() instanceof File)) return;
            File f = (File)node.getUserObject();
            showTreeCtx(f, e.getX(), e.getY());
        }
    }

    private void showTreeCtx(File f, int x, int y) {
        JPopupMenu pm = new JPopupMenu();
        pm.setBackground(T.BG3);
        pm.setBorder(BorderFactory.createLineBorder(T.BORDER));

        addCtxItem(pm, "New File",    () -> createNewFile(f.isDirectory() ? f : f.getParentFile()));
        addCtxItem(pm, "New Folder",  () -> createNewFolder(f.isDirectory() ? f : f.getParentFile()));
        pm.addSeparator();
        addCtxItem(pm, "Copy",        () -> clip.copy(f));
        addCtxItem(pm, "Cut",         () -> clip.cut(f));
        JMenuItem paste = ctxItem("Paste"); paste.setEnabled(clip.has());
        paste.addActionListener(e -> pasteToDir(f.isDirectory() ? f : f.getParentFile()));
        pm.add(paste);
        pm.addSeparator();
        addCtxItem(pm, "Rename",      () -> renameFile(f));
        addCtxItem(pm, "Delete",      () -> deleteFile(f));
        pm.show(fileTree, x, y);
    }

    private JMenuItem ctxItem(String t) {
        JMenuItem m = new JMenuItem(t);
        m.setBackground(T.BG3);
        m.setForeground(T.TEXT);
        m.setFont(new Font("Dialog", Font.PLAIN, 12));
        m.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        return m;
    }

    private void addCtxItem(JPopupMenu pm, String t, Runnable r) {
        JMenuItem m = ctxItem(t);
        m.addActionListener(e -> r.run());
        pm.add(m);
    }

    // ── WORK AREA ────────────────────────────────────────────────────────────
    private JSplitPane buildWorkArea() {
        tabs = new JTabbedPane(JTabbedPane.TOP) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(T.BG);
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        tabs.setBackground(T.BG2);
        tabs.setForeground(T.TEXT2);
        tabs.setFont(new Font("Dialog", Font.PLAIN, 12));
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        UIManager.put("TabbedPane.tabsOverlapBorder", true);

        addNewTab("untitled.java", TEMPLATES.get("Java"));

        JSplitPane vSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabs, buildTermPanel());
        vSplit.setResizeWeight(0.72);
        vSplit.setDividerSize(3);
        vSplit.setBackground(T.BORDER);
        vSplit.setBorder(null);
        return vSplit;
    }

    // ── EDITOR TABS ──────────────────────────────────────────────────────────
    private void addNewTab(String name, String content) {
        JTextPane ed = new JTextPane() {
            @Override protected void paintComponent(Graphics g) {
                try {
                    Rectangle r = modelToView2D(getCaretPosition()).getBounds();
                    g.setColor(T.LINE_HIGHLIGHT);
                    g.fillRect(0, r.y, getWidth(), r.height);
                } catch (Exception ignored) {}
                super.paintComponent(g);
            }
        };
        ed.setBackground(T.BG);
        ed.setForeground(T.TEXT);
        ed.setCaretColor(T.ACCENT2);
        ed.setSelectionColor(new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 55));
        ed.setFont(new Font("JetBrains Mono, Consolas, Monospaced", Font.PLAIN, 14));
        ed.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        ed.setText(content);
        ed.setCaretPosition(0);

        ed.getInputMap().put(KeyStroke.getKeyStroke("TAB"), new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                try { ed.getDocument().insertString(ed.getCaretPosition(), "    ", null); }
                catch (BadLocationException ignored) {}
            }
        });

        ed.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { scheduleHl(ed); updateCursor(ed); }
            public void removeUpdate(DocumentEvent e) { scheduleHl(ed); updateCursor(ed); }
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

        tabs.addChangeListener(e -> {
            int i = tabs.getSelectedIndex();
            if (i >= 0) {
                Component c = tabs.getComponentAt(i);
                if (c instanceof JScrollPane) {
                    Component v = ((JScrollPane)c).getViewport().getView();
                    if (v instanceof JTextPane) activeEd = (JTextPane)v;
                }
            }
        });
    }

    private JPanel makeTabComp(String name, int idx, JTextPane ed) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
        p.setOpaque(false);
        p.setBorder(null);

        JLabel ico = new JLabel(getLang(name).icon);
        ico.setFont(new Font("Dialog", Font.PLAIN, 12));
        p.add(ico);

        JLabel lbl = new JLabel(name);
        lbl.setFont(new Font("Dialog", Font.PLAIN, 12));
        lbl.setForeground(T.TEXT2);
        p.add(lbl);

        JButton x = new JButton("×");
        x.setFont(new Font("Dialog", Font.BOLD, 13));
        x.setForeground(T.TEXT3);
        x.setBackground(null);
        x.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
        x.setContentAreaFilled(false);
        x.setFocusPainted(false);
        x.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        x.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { x.setForeground(T.RED); }
            public void mouseExited(MouseEvent e) { x.setForeground(T.TEXT3); }
        });
        x.addActionListener(e -> {
            for (int i=0; i<tabs.getTabCount(); i++) {
                Component comp = tabs.getComponentAt(i);
                if (comp instanceof JScrollPane) {
                    if (((JScrollPane)comp).getViewport().getView() == ed) { tabs.remove(i); break; }
                }
            }
        });
        p.add(x);
        return p;
    }

    private void newTab() {
        addNewTab("untitled.java", TEMPLATES.get("Java"));
    }

    // ── TERMINAL PANEL ───────────────────────────────────────────────────────
    private JPanel buildTermPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 0));
        p.setBackground(T.BG2);

        JPanel tabBar = new JPanel(new BorderLayout());
        tabBar.setBackground(T.BG3);
        tabBar.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, T.BORDER));
        tabBar.setPreferredSize(new Dimension(0, 32));

        JPanel tabBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tabBtns.setBackground(T.BG3);
        for (String t : new String[]{"TERMINAL", "OUTPUT", "PROBLEMS"}) {
            JLabel tb = new JLabel(t);
            tb.setFont(new Font("Dialog", Font.PLAIN, 11));
            tb.setForeground(t.equals("TERMINAL") ? T.TEXT : T.TEXT3);
            tb.setBorder(BorderFactory.createCompoundBorder(
                t.equals("TERMINAL") ? BorderFactory.createMatteBorder(0, 0, 2, 0, T.ACCENT) : BorderFactory.createEmptyBorder(),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));
            tb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            tabBtns.add(tb);
        }
        tabBar.add(tabBtns, BorderLayout.WEST);

        JPanel tbRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 4));
        tbRight.setBackground(T.BG3);
        JButton clrBtn = new JButton("⊘ Clear");
        clrBtn.setFont(new Font("Dialog", Font.PLAIN, 11));
        clrBtn.setForeground(T.TEXT3);
        clrBtn.setBackground(null);
        clrBtn.setContentAreaFilled(false);
        clrBtn.setBorderPainted(false);
        clrBtn.setFocusPainted(false);
        clrBtn.addActionListener(e -> clearTerm());
        clrBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { clrBtn.setForeground(T.TEXT); }
            public void mouseExited(MouseEvent e) { clrBtn.setForeground(T.TEXT3); }
        });
        tbRight.add(clrBtn);
        tabBar.add(tbRight, BorderLayout.EAST);
        p.add(tabBar, BorderLayout.NORTH);

        terminal = new JTextPane();
        terminal.setBackground(T.BG);
        terminal.setForeground(T.TEXT);
        terminal.setFont(new Font("JetBrains Mono, Consolas, Monospaced", Font.PLAIN, 13));
        terminal.setMargin(new Insets(6, 12, 6, 12));
        terminal.setEditable(false);
        termDoc = terminal.getStyledDocument();

        JScrollPane tsp = new JScrollPane(terminal);
        tsp.setBorder(null);
        tsp.getViewport().setBackground(T.BG);
        tsp.getVerticalScrollBar().setUI(new ThinScrollBarUI());
        p.add(tsp, BorderLayout.CENTER);

        JPanel inputRow = new JPanel(new BorderLayout(0, 0));
        inputRow.setBackground(T.BG2);
        inputRow.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, T.BORDER));
        inputRow.setPreferredSize(new Dimension(0, 32));

        JLabel prompt = new JLabel("  ❯ ");
        prompt.setFont(new Font("Consolas", Font.BOLD, 13));
        prompt.setForeground(T.ACCENT2);
        JTextField cmdInput = new JTextField();
        cmdInput.setBackground(T.BG2);
        cmdInput.setForeground(T.TEXT);
        cmdInput.setCaretColor(T.ACCENT2);
        cmdInput.setFont(new Font("Consolas", Font.PLAIN, 13));
        cmdInput.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 4));
        cmdInput.addActionListener(e -> execCmd(cmdInput.getText(), cmdInput));

        inputRow.add(prompt, BorderLayout.WEST);
        inputRow.add(cmdInput, BorderLayout.CENTER);
        p.add(inputRow, BorderLayout.SOUTH);
        return p;
    }

    private void execCmd(String cmd, JTextField input) {
        if (cmd==null || cmd.trim().isEmpty()) return;
        String c = cmd.trim();
        tprint("\n  ❯ " + c + "\n", T.ACCENT2);
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
                int code = proc.waitFor();
                publish("\n  ─── exit " + code + " ───\n");
                return null;
            }
            protected void process(List<String> chunks) {
                for (String s : chunks) tprint(s, T.TEXT);
            }
        }.execute();
    }

    // ── SYNTAX HIGHLIGHTING ─────────────────────────────────────────────────
    private void scheduleHl(JTextPane ed) {
        if (hlTimer != null) hlTimer.stop();
        hlTimer = new javax.swing.Timer(250, e -> applyHl(ed));
        hlTimer.setRepeats(false);
        hlTimer.start();
    }

    private void applyHl(JTextPane ed) {
        StyledDocument doc = ed.getStyledDocument();
        String text;
        try { text = doc.getText(0, doc.getLength()); } catch (BadLocationException e) { return; }

        SimpleAttributeSet base = new SimpleAttributeSet();
        StyleConstants.setForeground(base, T.TEXT);
        doc.setCharacterAttributes(0, text.length(), base, true);

        String lang = (String) langBox.getSelectedItem();
        if (lang == null) return;

        switch (lang) {
            case "Java","Kotlin","C++","C","JavaScript","TypeScript","Rust","Go","Scala" -> {
                hl(doc, text, "/\\*[\\s\\S]*?\\*/", T.C_COMMENT);
                hl(doc, text, "//[^\\n]*", T.C_COMMENT);
                hl(doc, text, "`[^`]*`|\"[^\"]*\"|'[^']*'", T.C_STRING);
                hl(doc, text, "\\b(0x[0-9a-fA-F]+|\\d+\\.?\\d*)\\b", T.C_NUMBER);
                hl(doc, text, "[{}()\\[\\]]", T.C_BRACKET);
            }
            case "Python" -> {
                hl(doc, text, "#[^\\n]*", T.C_COMMENT);
                hl(doc, text, "\"\"\"[\\s\\S]*?\"\"\"|'''[\\s\\S]*?'''|\"[^\"]*\"|'[^']*'", T.C_STRING);
                hl(doc, text, "\\b\\d+\\.?\\d*\\b", T.C_NUMBER);
            }
            case "HTML/CSS/JS" -> {
                hl(doc, text, "<!--[\\s\\S]*?-->", T.C_COMMENT);
                hl(doc, text, "\"[^\"]*\"|'[^']*'", T.C_STRING);
                hl(doc, text, "</?" + "[a-zA-Z][a-zA-Z0-9\\-]*", T.C_FUNCTION);
                hl(doc, text, " [a-zA-Z\\-]+(?==)", T.C_KEYWORD);
            }
        }

        String kw = switch (lang) {
            case "Java" ->    "\\b(public|private|protected|class|interface|enum|extends|implements|static|final|void|new|return|if|else|for|while|do|try|catch|finally|throw|throws|import|package|this|super|null|true|false|int|long|double|float|boolean|char|byte|short|String|var|record|sealed|permits)\\b";
            case "Python" ->  "\\b(def|class|import|from|as|return|if|elif|else|for|while|try|except|finally|with|pass|break|continue|lambda|yield|raise|in|not|and|or|is|None|True|False|self|super|async|await)\\b";
            case "JavaScript","TypeScript" -> "\\b(const|let|var|function|class|extends|return|if|else|for|while|do|try|catch|finally|import|export|default|new|this|null|undefined|true|false|async|await|of|in|typeof|instanceof|switch|case|break|continue|throw|from|static|interface|type|enum)\\b";
            case "C","C++" ->  "\\b(int|long|double|float|char|void|bool|auto|const|static|return|if|else|for|while|do|struct|class|namespace|using|include|define|typedef|sizeof|new|delete|template|typename|public|private|protected|virtual|override|nullptr|true|false)\\b";
            case "Rust" ->    "\\b(fn|let|mut|pub|use|mod|struct|enum|impl|trait|for|while|loop|if|else|match|return|self|Self|super|crate|const|static|type|where|async|await|move|ref|box|dyn|unsafe|extern|true|false|i32|i64|u32|u64|f32|f64|bool|String|Vec|Option|Result)\\b";
            case "Go" ->      "\\b(func|var|const|type|struct|interface|map|chan|go|defer|select|switch|case|default|return|if|else|for|range|break|continue|import|package|true|false|nil|make|new|append|len|cap|string|int|bool|float64|byte|error)\\b";
            default -> null;
        };
        if (kw != null) hl(doc, text, kw, T.C_KEYWORD);

        hl(doc, text, "\\b[a-zA-Z_][a-zA-Z0-9_]*(?=\\s*\\()", T.C_FUNCTION);
        hl(doc, text, "\\b[A-Z][a-zA-Z0-9]*\\b", T.C_CLASS);
    }

    private void hl(StyledDocument doc, String text, String regex, Color c) {
        SimpleAttributeSet a = new SimpleAttributeSet();
        StyleConstants.setForeground(a, c);
        try {
            Matcher m = Pattern.compile(regex).matcher(text);
            while (m.find()) doc.setCharacterAttributes(m.start(), m.end()-m.start(), a, false);
        } catch (Exception ignored) {}
    }

    // ── LINE GUTTER ──────────────────────────────────────────────────────────
    class LineGutter extends JPanel implements DocumentListener, CaretListener {
        private final JTextComponent ed;
        LineGutter(JTextComponent ed) {
            this.ed = ed;
            setPreferredSize(new Dimension(52, 0));
            setBackground(T.BG);
            setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, T.BORDER));
            ed.getDocument().addDocumentListener(this);
            ed.addCaretListener(this);
        }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(new Font("Consolas", Font.PLAIN, 13));
            FontMetrics fm = g2.getFontMetrics();
            Rectangle clip = g.getClipBounds();
            Element root = ed.getDocument().getDefaultRootElement();
            int s = root.getElementIndex(ed.viewToModel2D(new Point(0, clip.y)));
            int e = root.getElementIndex(ed.viewToModel2D(new Point(0, clip.y + clip.height)));
            int caret = root.getElementIndex(ed.getCaretPosition());
            for (int i=s; i<=e; i++) {
                try {
                    Rectangle r = ed.modelToView2D(root.getElement(i).getStartOffset()).getBounds();
                    if (i == caret) {
                        g2.setColor(T.BG);
                        g2.fillRect(0, r.y, getWidth(), r.height);
                        g2.setColor(T.TEXT2);
                    } else {
                        g2.setColor(T.TEXT3);
                    }
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
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(T.ACCENT3);
        bar.setPreferredSize(new Dimension(0, 24));
        bar.setBorder(null);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setBackground(T.ACCENT3);
        JLabel gitLbl = statusItem("⎇  main", true);
        left.add(gitLbl);
        JLabel errLbl = statusItem("✗ 0   ⚠ 0", false);
        left.add(errLbl);
        bar.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setBackground(T.ACCENT3);
        right.add(statusItem("Java", false));
        cursorLbl = new JLabel("  Ln 1, Col 1  ");
        cursorLbl.setFont(new Font("Dialog", Font.PLAIN, 11));
        cursorLbl.setForeground(new Color(255,255,255,200));
        right.add(cursorLbl);
        right.add(statusItem("UTF-8", false));
        right.add(statusItem("CRLF", false));
        bar.add(right, BorderLayout.EAST);

        statusLbl = new JLabel("  Nova IDE — Ready");
        statusLbl.setFont(new Font("Dialog", Font.PLAIN, 11));
        statusLbl.setForeground(new Color(255,255,255,200));
        bar.add(statusLbl, BorderLayout.CENTER);

        return bar;
    }

    // FIXED: removed bogus getModel() override
    private JLabel statusItem(String t, boolean bold) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Dialog", bold ? Font.BOLD : Font.PLAIN, 11));
        l.setForeground(new Color(255,255,255,200));
        l.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return l;
    }

    private void setStatus(String t, Color c) {
        SwingUtilities.invokeLater(() -> {
            statusLbl.setText("  " + t);
            statusLbl.setForeground(c);
        });
    }

    private void updateCursor(JTextPane ed) {
        if (cursorLbl == null) return;
        StyledDocument doc = ed.getStyledDocument();
        int pos = ed.getCaretPosition();
        try {
            int line = doc.getDefaultRootElement().getElementIndex(pos) + 1;
            int lineStart = doc.getDefaultRootElement().getElement(line-1).getStartOffset();
            int col = pos - lineStart + 1;
            cursorLbl.setText("  Ln " + line + ", Col " + col + "  ");
        } catch (Exception ignored) {}
    }

    // ── THEME SWITCHING ──────────────────────────────────────────────────────
    private void applyNewTheme(String name) {
        T = THEMES.getOrDefault(name, THEMES.get("Nova Dark"));
        Cfg.setTheme(name);
        getContentPane().setBackground(T.BG);
        SwingUtilities.updateComponentTreeUI(this);
        repaint();
        tprint("🎨  Theme → " + name + "\n", T.ACCENT2);
    }

    // ── FILE OPS (SECURED) ───────────────────────────────────────────────────
    private boolean isSafeName(String name) {
        if (name == null || name.trim().isEmpty()) return false;
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
        int idx = tabs.getSelectedIndex();
        if (idx < 0) return;
        String name = tabs.getTitleAt(idx);
        JFileChooser fc = new JFileChooser(Cfg.getLastDir());
        fc.setSelectedFile(new File(name));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try {
                Files.writeString(f.toPath(), activeEd.getText());
                tprint("💾  Saved → " + f.getName() + "\n", T.GREEN);
                setStatus("Saved — " + f.getName(), T.GREEN);
            } catch (IOException e) { tprint("❌  " + e.getMessage() + "\n", T.RED); }
        }
    }

    private void openFileInEditor(File f) {
        try {
            String content = Files.readString(f.toPath());
            addNewTab(f.getName(), content);
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
        if (f.isDirectory()) { File[] ch = f.listFiles(); if (ch!=null) for (File c : ch) delRec(c); }
        f.delete();
    }

    private void pasteToDir(File dest) {
        if (!clip.has()) return;
        File target = new File(dest, clip.src.getName());
        try {
            if (clip.isCopy()) copyRec(clip.src, target);
            else { clip.src.renameTo(target); }
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
        if (projectRoot==null || !projectRoot.exists()) return;
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
        for (Enumeration<?> e = node.children(); e.hasMoreElements(); ) {
            expandAll(tree, parent.pathByAddingChild(e.nextElement()), expand);
        }
        if (expand) tree.expandPath(parent);
    }

    // ── RUN CODE ─────────────────────────────────────────────────────────────
    private void runCode() {
        if (activeEd == null) { tprint("❌  No active editor\n", T.RED); return; }
        String lang = (String) langBox.getSelectedItem();
        String code = activeEd.getText();
        if (code == null || code.trim().isEmpty()) { tprint("❌  Empty file\n", T.RED); return; }

        if ("HTML/CSS/JS".equals(lang)) { toggleLive(); return; }

        tprint("\n  ── Running " + lang + " ──────────────────────\n", T.TEXT3);
        setStatus("Running " + lang + "...", T.ACCENT2);

        boolean ext = runModeBox.getSelectedIndex() == 1;
        new SwingWorker<Void, String>() {
            protected Void doInBackground() throws Exception {
                File tmp = Files.createTempDirectory("nova_").toFile(); tmp.deleteOnExit();
                String[] cmd = buildCmd(lang, code, tmp);
                if (cmd == null) { publish("❌  " + lang + " not supported\n"); return null; }
                if (ext) {
                    buildExternal(cmd, tmp).start();
                    publish("  [Opened in external terminal]\n");
                    return null;
                }
                ProcessBuilder pb = new ProcessBuilder(cmd).redirectErrorStream(true);
                pb.directory(tmp);
                runProcess = pb.start();
                try (BufferedReader r = new BufferedReader(new InputStreamReader(runProcess.getInputStream()))) {
                    String line;
                    while ((line = r.readLine()) != null) publish(line + "\n");
                }
                int ec = runProcess.waitFor();
                publish("\n  ── exit " + ec + " ──────────────────────\n");
                return null;
            }
            protected void process(List<String> chunks) {
                for (String s : chunks) {
                    boolean isErr = s.toLowerCase().contains("error") || s.contains("exit 1") || s.contains("Exception");
                    tprint(s, isErr ? T.RED : T.TEXT);
                }
            }
            protected void done() {
                try { get(); setStatus("Done ✓", T.GREEN); }
                catch (Exception e) { setStatus("Error", T.RED); tprint("❌  " + e.getMessage() + "\n", T.RED); }
                runProcess = null;
            }
        }.execute();
    }

    private String[] buildCmd(String lang, String code, File dir) throws IOException {
        return switch (lang) {
            case "Java" -> {
                Matcher m = Pattern.compile("(?:public\\s+)?class\\s+(\\w+)").matcher(code);
                String cls = m.find() ? m.group(1) : "Main";
                // SECURITY: sanitize class name to prevent command injection
                if (!cls.matches("[a-zA-Z_][a-zA-Z0-9_]*")) cls = "Main";
                File src = new File(dir, cls + ".java");
                Files.writeString(src.toPath(), code);
                String jc = "javac -encoding UTF-8 \"" + src.getAbsolutePath() + "\" -d \"" + dir.getAbsolutePath() + "\" && java -cp \"" + dir.getAbsolutePath() + "\" " + cls;
                yield Cfg.isWin() ? new String[]{"cmd","/c",jc} : new String[]{"/bin/sh","-c",jc.replace("\"","'")};
            }
            case "Python" -> {
                File f = new File(dir,"main.py"); Files.writeString(f.toPath(),code);
                yield new String[]{Cfg.findCmd("python","python3"), f.getAbsolutePath()};
            }
            case "JavaScript" -> {
                File f = new File(dir,"main.js"); Files.writeString(f.toPath(),code);
                yield new String[]{Cfg.findCmd("node"), f.getAbsolutePath()};
            }
            case "C++" -> {
                File f = new File(dir,"main.cpp"); Files.writeString(f.toPath(),code);
                String out = new File(dir, Cfg.isWin()?"main.exe":"main.out").getAbsolutePath();
                String c = Cfg.findCmd("g++","clang++") + " -std=c++20 \"" + f.getAbsolutePath() + "\" -o \"" + out + "\" && \"" + out + "\"";
                yield Cfg.isWin() ? new String[]{"cmd","/c",c} : new String[]{"/bin/sh","-c",c.replace("\"","'")};
            }
            case "C" -> {
                File f = new File(dir,"main.c"); Files.writeString(f.toPath(),code);
                String out = new File(dir, Cfg.isWin()?"main.exe":"main.out").getAbsolutePath();
                String c = Cfg.findCmd("gcc","clang") + " \"" + f.getAbsolutePath() + "\" -o \"" + out + "\" && \"" + out + "\"";
                yield Cfg.isWin() ? new String[]{"cmd","/c",c} : new String[]{"/bin/sh","-c",c.replace("\"","'")};
            }
            case "Rust" -> {
                File f = new File(dir,"main.rs"); Files.writeString(f.toPath(),code);
                String out = new File(dir,"main").getAbsolutePath();
                String c = "rustc \"" + f.getAbsolutePath() + "\" -o \"" + out + (Cfg.isWin()?".exe":"") + "\" && \"" + out + (Cfg.isWin()?".exe":"") + "\"";
                yield Cfg.isWin() ? new String[]{"cmd","/c",c} : new String[]{"/bin/sh","-c",c.replace("\"","'")};
            }
            case "Go" -> {
                File f = new File(dir,"main.go"); Files.writeString(f.toPath(),code);
                yield new String[]{"go","run",f.getAbsolutePath()};
            }
            default -> null;
        };
    }

    private ProcessBuilder buildExternal(String[] cmd, File dir) {
        String c = String.join(" ", cmd);
        if (Cfg.isWin()) {
            return Cfg.cmdExists("wt") ?
                new ProcessBuilder("wt","new-tab","--title","Nova Run","cmd","/c",c+" & pause") :
                new ProcessBuilder("cmd","/c","start","cmd","/k",c+" & pause");
        } else {
            if (Cfg.cmdExists("gnome-terminal"))
                return new ProcessBuilder("gnome-terminal","--","/bin/sh","-c",c+"; read");
            if (Cfg.cmdExists("xterm"))
                return new ProcessBuilder("xterm","-e","/bin/sh","-c",c+"; read");
            return new ProcessBuilder("/bin/sh","-c",c);
        }
    }

    private void stopProcess() {
        if (runProcess != null && runProcess.isAlive()) {
            runProcess.destroyForcibly();
            tprint("\n  ⏹  Stopped.\n", T.YELLOW);
            setStatus("Stopped", T.YELLOW);
        }
    }

    // ── LIVE SERVER (SECURED) ────────────────────────────────────────────────
    private void toggleLive() {
        if (liveOn) { stopLive(); return; }
        try {
            int port = Cfg.getLivePort();
            Path root = Files.createTempDirectory("nova_live_");
            Files.writeString(root.resolve("index.html"), injectReload(activeEd.getText()));

            // SECURITY: bind only to localhost
            liveServer = HttpServer.create(new InetSocketAddress("127.0.0.1", port), 0);
            liveServer.createContext("/", ex -> {
                String path = ex.getRequestURI().getPath().equals("/") ? "/index.html" : ex.getRequestURI().getPath();
                // SECURITY: prevent path traversal
                Path requested = root.resolve(path.substring(1)).normalize();
                if (!requested.startsWith(root)) {
                    byte[] b = "403 Forbidden".getBytes();
                    ex.sendResponseHeaders(403, b.length);
                    ex.getResponseBody().write(b); ex.getResponseBody().close();
                    return;
                }
                File f = requested.toFile();
                ex.getResponseHeaders().add("Cache-Control", "no-cache");
                // SECURITY: restrict CORS to localhost
                ex.getResponseHeaders().add("Access-Control-Allow-Origin", "http://127.0.0.1:" + port);
                if (!f.exists() || f.isDirectory()) {
                    byte[] b = "404".getBytes(); ex.sendResponseHeaders(404, b.length);
                    ex.getResponseBody().write(b); ex.getResponseBody().close(); return;
                }
                byte[] body = Files.readAllBytes(f.toPath());
                if (f.getName().endsWith(".html")) body = injectReload(new String(body, StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8);
                ex.getResponseHeaders().add("Content-Type", mime(f.getName()));
                ex.sendResponseHeaders(200, body.length);
                ex.getResponseBody().write(body); ex.getResponseBody().close();
            });
            liveServer.setExecutor(Executors.newCachedThreadPool());
            liveServer.start();
            liveOn = true;

            String url = "http://127.0.0.1:" + port;
            liveLbl.setText("  ⬡ LIVE :" + port + "  ");
            liveLbl.setForeground(T.GREEN);
            tprint("⬡  Live Server → " + url + "\n", T.CYAN);

            if (Desktop.isDesktopSupported()) Desktop.getDesktop().browse(new URI(url));

            new Thread(() -> {
                long last = 0;
                while (liveOn) {
                    try {
                        Thread.sleep(400);
                        if (activeEd != null) {
                            long cur = activeEd.getText().hashCode();
                            if (cur != last) { Files.writeString(root.resolve("index.html"), injectReload(activeEd.getText())); last = cur; }
                        }
                    } catch (Exception ignored) {}
                }
            }, "LiveWatcher").start();
        } catch (Exception e) { tprint("❌  " + e.getMessage() + "\n", T.RED); }
    }

    private void stopLive() {
        if (liveServer != null) liveServer.stop(0);
        liveOn = false;
        liveLbl.setText("  ⬡ LIVE OFF  ");
        liveLbl.setForeground(T.TEXT3);
        tprint("⏹  Live Server stopped.\n", T.YELLOW);
    }

    private String injectReload(String html) {
        String s = "<script>setInterval(async()=>{try{const r=await fetch(location.href,{cache:'no-store'});const t=await r.text();if(window._t&&window._t!==t)location.reload();window._t=t;}catch(e){}},800);</script>";
        return html.contains("</body>") ? html.replace("</body>", s+"</body>") : html + s;
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
            StyleConstants.setFontFamily(a, "JetBrains Mono, Consolas");
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

    // ── WELCOME + TOOLS CHECK ────────────────────────────────────────────────
    private void printWelcome() {
        tprint("  Nova IDE  —  Ready\n", T.ACCENT2);
        tprint("  ─────────────────────────────────────────\n", T.TEXT3);
        tprint("  Ctrl+Enter  Run code\n", T.TEXT2);
        tprint("  Ctrl+S      Save file\n", T.TEXT2);
        tprint("  Ctrl+F      Find in editor\n", T.TEXT2);
        tprint("  ─────────────────────────────────────────\n\n", T.TEXT3);
    }

    private void checkTools() {
        for (String t : new String[]{"javac","python","python3","node","g++","rustc","go"}) {
            if (!Cfg.cmdExists(t))
                SwingUtilities.invokeLater(() -> tprint("  ⚠  " + t + " not found in PATH\n", T.YELLOW));
        }
    }

    // ── SHORTCUTS ────────────────────────────────────────────────────────────
    private void setupShortcuts() {
        JRootPane r = getRootPane();
        bind(r, KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK, "run", e -> runCode());
        bind(r, KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK, "save", e -> saveFile());
        bind(r, KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK, "find", e -> showFind());
        bind(r, KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK, "newtab", e -> newTab());
        bind(r, KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK, "closetab", e -> {
            int i = tabs.getSelectedIndex();
            if (i >= 0) tabs.remove(i);
        });
    }

    private void bind(JRootPane r, int key, int mod, String name, ActionListener a) {
        r.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key, mod), name);
        r.getActionMap().put(name, new AbstractAction() { public void actionPerformed(ActionEvent e) { a.actionPerformed(e); }});
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

    // ── MAIN ─────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(NovaIDE::new);
    }
}