import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Caret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Element;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.spi.FileTypeDetector;
import java.security.Key;

public class TextEditor {

    private static JFrame main_frame,font_frame;
    private static JMenuBar main_menu_bar;
    private static JMenu file,edit,format,view,about_us;
    private static JTextArea write_area,no_lines,bottom_LineCol;
    private static JMenuItem file_exit,file_new,file_save,file_open,edit_cut,edit_copy,edit_paste,format_font,format_darkTheme,format_lightTheme;
    private static JScrollPane jsp;
    private static Font font_Serif,font_SansSerif,font_Monospace;
    private static JComboBox font_NameSelector,font_SizeSelector;
    private static String[] font_Names = {"SansSerif","Serif","Monospace"};
    private static String[] font_Size = {"8","9","10","11","12","14","16","18","20","22","24","26","28","32","48"};
    private static JLabel fontNameLabel,fontSizeLabel,bottom_rowNo,bottom_colNo;
    private static Color dark_primaryBackground,dark_secondaryBackground,dark_primaryForeground,dark_secondaryForeground,dark_caret,dark_lineNumberingColor;
    private static Color light_primaryBackground,light_secondaryBackground,light_primaryForeground,light_secondaryForeground,light_caret,light_lineNumberingColor;

    TextEditor()
    {
        main_frame = new JFrame();
        font_frame = new JFrame("Font Selector");
        main_menu_bar = new JMenuBar();
        jsp = new JScrollPane();
        file = new JMenu("File");
        edit = new JMenu("Edit");
        format = new JMenu("Format");
        view = new JMenu("View");
        about_us = new JMenu("About Us");
        write_area = new JTextArea("");
        no_lines = new JTextArea("1");
        bottom_LineCol = new JTextArea("");
        file_new = new JMenuItem("New         ");
        file_save = new JMenuItem("Save         ");
        file_open = new JMenuItem("Open         ");
        file_exit = new JMenuItem("Exit         ");
        format_font = new JMenuItem("Font         ");
        edit_cut = new JMenuItem(new DefaultEditorKit.CutAction());
        edit_cut.setText("Cut");
        edit_copy = new JMenuItem(new DefaultEditorKit.CopyAction());
        edit_copy.setText("Copy");
        edit_paste = new JMenuItem(new DefaultEditorKit.PasteAction());
        edit_paste.setText("Paste");
        format_darkTheme = new JMenuItem("Dark Theme");
        format_lightTheme = new JMenuItem("Light Theme");
        fontNameLabel = new JLabel("Font Name");
        fontSizeLabel = new JLabel("Font Size");
        bottom_colNo = new JLabel("");
        bottom_rowNo = new JLabel("");
        font_NameSelector = new JComboBox(font_Names);
        font_SizeSelector = new JComboBox(font_Size);
        font_Serif = new Font(Font.SERIF,Font.PLAIN,12);
        font_SansSerif = new Font(Font.SANS_SERIF,Font.PLAIN,12);
        font_Monospace = new Font(Font.MONOSPACED,Font.PLAIN,12);

        // Dark Theme Colors
        dark_primaryBackground = new Color(36,36,36);
        dark_secondaryBackground = new Color(72,72,72);
        dark_primaryForeground = new Color(240,240,240);
        dark_secondaryForeground = new Color(201,159,252);
        dark_caret = new Color(255,144,0);
        dark_lineNumberingColor = new Color(0,153,51);

        // Light Theme Colors
        light_primaryBackground = new Color(255,255,255); // 220 , 184
        light_secondaryBackground = new Color(220,220,220);
        light_primaryForeground = new Color(16,16,16);
        light_secondaryForeground = new Color(255, 79, 88);
        light_caret = new Color(0,0,0);
        light_lineNumberingColor = new Color(0,0,255);

        // main_frame settings
        main_frame.setSize(500,500);
        main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main_frame.setLocationRelativeTo(null);
        main_frame.setTitle("NotePad by Us");

        DarkTheme();

        // Scroll Settings
        // To not have any border
        jsp.setBorder(BorderFactory.createEmptyBorder());

        // Menu Bar Settings
        // To not have any border
        main_menu_bar.setBorder(BorderFactory.createEmptyBorder());

        // write_area settings
        write_area.setLineWrap(false);   // Sets whether lines are wrapped if they are too long to fit within the allocated width
        write_area.setWrapStyleWord(false);  // Sets whether lines can be wrapped at white space (word boundaries) or at any character.
        // To not have any border
        // write_area.setBorder(BorderFactory.createEmptyBorder());
        // write_area.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(255,144,0)),BorderFactory.createEmptyBorder(0,5,0,5)));

        // Displaying No of lines
        no_lines.setEditable(false);

        write_area.getDocument().addDocumentListener(new DocumentListener() {
            public String getText()
            {
                int caretPosition = write_area.getDocument().getLength();
                Element root = write_area.getDocument().getDefaultRootElement();    // System.getProperty("line.separator") same as \n in C
                String text = "1" + System.getProperty("line.separator");
                for (int i=2;i<root.getElementIndex(caretPosition)+2;i++)
                {
                    text += i + System.getProperty("line.separator");
                }
                return text;
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                no_lines.setText(getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                no_lines.setText(getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                no_lines.setText(getText());
            }
        });

        bottom_LineCol.setEditable(false);
        bottom_LineCol.setBorder(BorderFactory.createEmptyBorder());

        ShortCut();

        // Functioning of file_new
        file_new.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                write_area.setText("");
            }
        });

        // Functioning of file_exit
        file_exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Functioning of file_save
        file_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // To make UI for Save look like that of Windows.
                try
                {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }catch(Exception e1){

                }
                JFileChooser fileChooser = new JFileChooser();
                int option = fileChooser.showSaveDialog(main_frame);
                File file = fileChooser.getSelectedFile();
                if(option == JFileChooser.APPROVE_OPTION)
                {
                    try {
                        FileWriter wr = new FileWriter(file+".txt");

                        BufferedWriter w = new BufferedWriter(wr);
                        w.write(write_area.getText());
                        w.close();
                    } catch (IOException ioException) {
                    }

                }
            }
        });

        // Functioning of file_open
        file_open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // To make UI for Open look like that of Windows.
                try
                {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }catch(Exception e2){

                }
                JFileChooser fileChooser = new JFileChooser();
                int option = fileChooser.showOpenDialog(main_frame);
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                if(option == JFileChooser.APPROVE_OPTION)
                {
                    try
                    {
                        String s1 = "",sl = "";
                        FileReader fr = new FileReader(file);

                        BufferedReader br = new BufferedReader(fr);

                        sl = br.readLine();

                        while ((s1 = br.readLine()) != null)
                        {
                            sl = sl + "\n" + s1;
                        }

                        write_area.setText(sl);

                    } catch (Exception evt)
                    {
                    }
                }
            }
        });

        // Functioning of format_font
        format_font.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                font_frame.setSize(300,350);
                font_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                font_frame.setLocationRelativeTo(null);
                font_frame.setVisible(true);
                FontSelector();
                font_frame.addWindowListener(new WindowListener() {
                    @Override
                    public void windowOpened(WindowEvent e) {
                        main_frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                    }

                    @Override
                    public void windowClosing(WindowEvent e) {

                    }

                    @Override
                    public void windowClosed(WindowEvent e) {
                        main_frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    }

                    @Override
                    public void windowIconified(WindowEvent e) {

                    }

                    @Override
                    public void windowDeiconified(WindowEvent e) {

                    }

                    @Override
                    public void windowActivated(WindowEvent e) {

                    }

                    @Override
                    public void windowDeactivated(WindowEvent e) {

                    }
                });
            }
        });

        // Functioning of format_darkTheme
        format_darkTheme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DarkTheme();
            }
        });

        // Functioning of format_lightTheme
        format_lightTheme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LightTheme();
            }
        });

        // Adding all Elements
        file.add(file_new);
        file.add(file_save);
        file.add(file_open);
        file.add(file_exit);

        edit.add(edit_cut);
        edit.add(edit_copy);
        edit.add(edit_paste);

        format.add(format_font);
        format.add(format_darkTheme);
        format.add(format_lightTheme);

        main_menu_bar.add(file);
        main_menu_bar.add(edit);
        main_menu_bar.add(format);
        main_menu_bar.add(view);
        main_menu_bar.add(about_us);

        bottom_LineCol.add(bottom_colNo);
        bottom_LineCol.add(bottom_rowNo);

        main_frame.getContentPane().add(BorderLayout.NORTH,main_menu_bar);
        // main_frame.getContentPane().add(BorderLayout.SOUTH,bottom_LineCol);
        jsp.getViewport().add(write_area);
        jsp.setRowHeaderView(no_lines);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        main_frame.getContentPane().add(BorderLayout.CENTER,jsp);

        main_frame.setVisible(true);

    }

    public void ShortCut()
    {
        file.setMnemonic(KeyEvent.VK_F);

        file_save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK)); // For accessing shortcuts with cntr.
        file_save.setMnemonic(KeyEvent.VK_S);

        file_exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));

        file_new.setMnemonic(KeyEvent.VK_N);
        file_new.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));

        file_open.setMnemonic(KeyEvent.VK_O);
        file_open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));  // For accessing shortcuts with cntr.

        edit.setMnemonic(KeyEvent.VK_E);

        edit_cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,ActionEvent.CTRL_MASK));
//        edit_cut.setMnemonic(KeyEvent.VK_T);  // Check this

        edit_copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK));
        edit_copy.setMnemonic(KeyEvent.VK_C);

        edit_paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK));
        edit_paste.setMnemonic(KeyEvent.VK_P);

        edit_cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,ActionEvent.CTRL_MASK));
        edit_cut.setMnemonic(KeyEvent.VK_X);

        format_font.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));

    }

    public void FontSelector()
    {
        font_NameSelector.setBounds(25,50,90,20);
        font_SizeSelector.setBounds(175,50,90,20);
        fontNameLabel.setBounds(25,30,90,20);
        fontNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        fontSizeLabel.setBounds(175,30,90,20);
        fontSizeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        font_NameSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = (String) font_NameSelector.getSelectedItem();

                float no = Float.parseFloat((String) font_SizeSelector.getSelectedItem());

                switch (s)
                {
                    case "Serif":
                        write_area.setFont(font_Serif);
                        no_lines.setFont(font_Serif);
                        SizeSelector(no);
                        break;

                    case "SansSerif":
                        write_area.setFont(font_SansSerif);
                        no_lines.setFont(font_SansSerif);
                        SizeSelector(no);
                        break;

                    case "Monospace":
                        write_area.setFont(font_Monospace);
                        no_lines.setFont(font_Monospace);
                        SizeSelector(no);
                        break;

                    default:
                        break;
                }

            }
        });

        font_SizeSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                float no = Float.parseFloat((String) font_SizeSelector.getSelectedItem());

                SizeSelector(no);

            }
        });

        font_frame.add(fontNameLabel);
        font_frame.add(fontSizeLabel);

        font_frame.add(font_SizeSelector);
        font_frame.add(font_NameSelector);

        font_frame.setLayout(null);


    }

    public void SizeSelector(float no)
    {
        write_area.setFont(write_area.getFont().deriveFont(no));
        no_lines.setFont(no_lines.getFont().deriveFont(no));
    }

    public void DarkTheme()
    {
        //
        write_area.setBackground(dark_primaryBackground);
        write_area.setForeground(dark_primaryForeground);
        write_area.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(dark_primaryBackground),BorderFactory.createEmptyBorder(0,5,20,0)));

        //
        main_menu_bar.setBackground(dark_secondaryBackground);

        //
        no_lines.setBackground(dark_primaryBackground);
        no_lines.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(dark_primaryBackground),BorderFactory.createEmptyBorder(0,5,0,5)));
        no_lines.setForeground(dark_lineNumberingColor);

        //
        write_area.setCaretColor(dark_caret);

        //
        font_frame.getContentPane().setBackground(dark_primaryBackground);

        //
        file.setForeground(dark_secondaryForeground);
        edit.setForeground(dark_secondaryForeground);
        format.setForeground(dark_secondaryForeground);
        view.setForeground(dark_secondaryForeground);
        about_us.setForeground(dark_secondaryForeground);

        //
        file_exit.setForeground(dark_secondaryForeground);
        file_exit.setBackground(dark_secondaryBackground);

        file_new.setForeground(dark_secondaryForeground);
        file_new.setBackground(dark_secondaryBackground);

        file_open.setForeground(dark_secondaryForeground);
        file_open.setBackground(dark_secondaryBackground);

        file_save.setForeground(dark_secondaryForeground);
        file_save.setBackground(dark_secondaryBackground);

        //
        edit_cut.setForeground(dark_secondaryForeground);
        edit_cut.setBackground(dark_secondaryBackground);

        edit_copy.setForeground(dark_secondaryForeground);
        edit_copy.setBackground(dark_secondaryBackground);

        edit_paste.setForeground(dark_secondaryForeground);
        edit_paste.setBackground(dark_secondaryBackground);

        //
        format_darkTheme.setForeground(dark_secondaryForeground);
        format_darkTheme.setBackground(dark_secondaryBackground);

        format_lightTheme.setForeground(dark_secondaryForeground);
        format_lightTheme.setBackground(dark_secondaryBackground);

        //
        format_font.setForeground(dark_secondaryForeground);
        format_font.setBackground(dark_secondaryBackground);

        fontNameLabel.setForeground(dark_primaryForeground);
        fontSizeLabel.setForeground(dark_primaryForeground);

    }

    public void LightTheme()
    {
        write_area.setForeground(light_primaryForeground);
        write_area.setBackground(light_primaryBackground);
        write_area.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(light_primaryBackground),BorderFactory.createEmptyBorder(0,5,0,0)));

        //
        main_menu_bar.setBackground(light_secondaryBackground);

        //
        no_lines.setBackground(light_primaryBackground);
        no_lines.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(light_primaryBackground),BorderFactory.createEmptyBorder(0,5,0,5)));
        no_lines.setForeground(light_lineNumberingColor);

        //
        write_area.setCaretColor(light_caret);

        //
        font_frame.getContentPane().setBackground(light_primaryBackground);

        //
        file.setForeground(light_secondaryForeground);
        edit.setForeground(light_secondaryForeground);
        format.setForeground(light_secondaryForeground);
        view.setForeground(light_secondaryForeground);
        about_us.setForeground(light_secondaryForeground);

        //
        file_exit.setForeground(light_secondaryForeground);
        file_exit.setBackground(light_secondaryBackground);

        file_new.setForeground(light_secondaryForeground);
        file_new.setBackground(light_secondaryBackground);

        file_open.setForeground(light_secondaryForeground);
        file_open.setBackground(light_secondaryBackground);

        file_save.setForeground(light_secondaryForeground);
        file_save.setBackground(light_secondaryBackground);

        //
        edit_cut.setForeground(light_secondaryForeground);
        edit_cut.setBackground(light_secondaryBackground);

        edit_copy.setForeground(light_secondaryForeground);
        edit_copy.setBackground(light_secondaryBackground);

        edit_paste.setForeground(light_secondaryForeground);
        edit_paste.setBackground(light_secondaryBackground);

        //
        format_darkTheme.setForeground(light_secondaryForeground);
        format_darkTheme.setBackground(light_secondaryBackground);

        format_lightTheme.setForeground(light_secondaryForeground);
        format_lightTheme.setBackground(light_secondaryBackground);

        format_font.setForeground(light_secondaryForeground);
        format_font.setBackground(light_secondaryBackground);

        fontNameLabel.setForeground(light_secondaryForeground);
        fontSizeLabel.setForeground(light_secondaryForeground);
    }

    public static void main(String[] args) {

        TextEditor textEditor = new TextEditor();
    }

}
