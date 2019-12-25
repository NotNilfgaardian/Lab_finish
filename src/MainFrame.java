import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;

public class MainFrame extends JFrame
{
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private JFileChooser fileChooser = null;

    private JCheckBoxMenuItem showAxisMenuItem;
    private JCheckBoxMenuItem showMarkerMenuItem;

    private GraphicsDisplay display = new GraphicsDisplay();

    private boolean fileLoaded = false;

    public static void main(String arg[])
    {
        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public MainFrame()
    {
        super("Построение графика функции на основе подготовленных файлов");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH) / 2, ((kit.getScreenSize()).height - HEIGHT) / 2);
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("Файл");
        Action openGraphicsAction = new AbstractAction("Открыть")
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (fileChooser == null)
                {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) ;
                openGraphics(fileChooser.getSelectedFile());
            }
        };
        fileMenu.add(openGraphicsAction);
        menuBar.add(fileMenu);

        JMenu graphicsMenu = new JMenu("График");
        Action showAxisAction = new AbstractAction("Показывать оси координат")
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                display.setShowAxis(showAxisMenuItem.isSelected());
            }
        };
        showAxisMenuItem = new JCheckBoxMenuItem(showAxisAction);
        graphicsMenu.add(showAxisMenuItem);
        showAxisMenuItem.setSelected(true);
        menuBar.add(graphicsMenu);

        Action showMarkersAction = new AbstractAction("Показывать маркеры точек")
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                display.setShowMarkers(showMarkerMenuItem.isSelected());
            }
        };
        showMarkerMenuItem = new JCheckBoxMenuItem(showMarkersAction);
        graphicsMenu.add(showMarkerMenuItem);
        showMarkerMenuItem.setSelected(true);
        JMenu donateMenu = new JMenu("Донат");
        menuBar.add(donateMenu);

        Action rekviziti = new AbstractAction("Реквезиты")
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(MainFrame.this, "Номер карты: 1311 2721 4059 4122 " + "\n" + "Номер мобильного: +375 33 360-20-86", "Реквезиты", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        donateMenu.add(rekviziti);
        JMenu spravkaMenu = new JMenu("Справка");
        menuBar.add(spravkaMenu);
        Action aboutProgram = new AbstractAction("О программе")
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(MainFrame.this, "Справок не выдаём", "Справка", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        spravkaMenu.add(aboutProgram);

        graphicsMenu.addMenuListener(new GraphicsMenuListener());
        getContentPane().add(display, BorderLayout.CENTER);

    }

    private class GraphicsMenuListener implements MenuListener
    {
        @Override
        public void menuSelected(MenuEvent e)
        {
            showAxisMenuItem.setEnabled(fileLoaded);
            showMarkerMenuItem.setEnabled(fileLoaded);
        }

        @Override
        public void menuDeselected(MenuEvent e)
        {

        }

        @Override
        public void menuCanceled(MenuEvent e)
        {

        }
    }

    protected void openGraphics(File selectedFile)
    {
        try
        {
            DataInputStream in = new DataInputStream(new FileInputStream(selectedFile));
            ArrayList graphicsData = new ArrayList(50);
            int i = 0;
            while (in.available() > 0)
            {
                Double x = in.readDouble();
                Double y = in.readDouble();
                graphicsData.add(new Double[]{x, y});
            }
            if (graphicsData != null && graphicsData.size() > 0)
            {
                fileLoaded = true;
                display.showGraphics(graphicsData);
            }
            in.close();
        } catch (FileNotFoundException e)
        {
            JOptionPane.showMessageDialog(MainFrame.this, "Указаный файл не найден", "Мы сожалеем", JOptionPane.WARNING_MESSAGE);
            return;
        } catch (IOException exp)
        {
            JOptionPane.showMessageDialog(MainFrame.this, "Ошибка чтения координат точек из файла", "Мы сожалеем", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }

}
