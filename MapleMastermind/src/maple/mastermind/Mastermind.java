package maple.mastermind;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;

/**
 * Simple algorithm to search for a 4 color mastermind
 * solution in a modified generated txt file with
 * moves taken from http://www.tnelson.demon.co.uk/mastermind/
 *
 */
public class Mastermind extends JFrame
{
  private static final long serialVersionUID = -5747485587934568180L;
  private List<String> list;
  private List<Integer> mode;
  private String line;
  private int index;
  private int ind;
  private boolean exit;
  private JTextArea area;
  private JButton goButton;
  private JComboBox combo;
  private JScrollPane jScrollPane1;
  private JButton restart;

  public Mastermind()
  {
    initComponents();
    init();
  }

  private void init()
  {
    setLocationRelativeTo(this);
    this.line = "MMSS";
    String[] items = { "None", "1 W", "1 OK", "2 W", "1 OK, 1 W", "2 OK", "3 W", "1 OK, 2 W", "2 OK, 1 W", "3 OK", "4 W", "1 OK, 3 W", "2 OK, 2 W" };

    this.combo.removeAllItems();
    for (int i = 0; i < items.length; i++) {
      this.combo.addItem(items[i]);
    }
    readFile();
    this.area.append("M - medal\n");
    this.area.append("S - scroll\n");
    this.area.append("P - potion\n");
    this.area.append("F - food\n\n");
    this.area.append("1st move is always MMSS!\n\n");
    this.mode = new ArrayList<Integer>();
  }

  /**
   * Auto generated in NetBeans
   */
  private void initComponents()
  {
    this.jScrollPane1 = new JScrollPane();
    this.area = new JTextArea();
    this.combo = new JComboBox();
    this.goButton = new JButton();
    this.restart = new JButton();

    setDefaultCloseOperation(3);
    setResizable(false);

    JLabel resultLabel = new JLabel("Result:");
    this.area.setColumns(20);
    this.area.setRows(5);
    this.jScrollPane1.setViewportView(this.area);

    this.combo.setModel(new DefaultComboBoxModel());

    this.goButton.setText("Go");
    this.goButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        Mastermind.this.buttonActionPerformed(evt);
      }
    });
    this.restart.setText("Restart");
    this.restart.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        Mastermind.this.restartActionPerformed(evt);
      }
    });
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.LEADING,
												false)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(resultLabel)
																.addGap(8)
																.addComponent(
																		this.combo,
																		-2,
																		197, -2)
																.addPreferredGap(
																		LayoutStyle.ComponentPlacement.RELATED,
																		-1,
																		32767)
																.addComponent(
																		this.goButton)
																.addGap(8)
																.addComponent(
																		this.restart))
												.addComponent(
														this.jScrollPane1, -2,
														365, -2))
								.addContainerGap(18, 32767)));

		layout.setVerticalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								layout.createParallelGroup(
										GroupLayout.Alignment.BASELINE)
										.addComponent(resultLabel)
										.addComponent(this.combo, -2, -1, -2)
										.addComponent(this.goButton)
										.addComponent(this.restart))
						.addGap(31, 31, 31)
						.addComponent(this.jScrollPane1, -1, 222, 32767)
						.addContainerGap()));

    pack();
  }

  private void buttonActionPerformed(ActionEvent evt)
  {
    if (this.mode == null) {
      return;
    }

    if (this.mode.isEmpty()) {
      this.line = "MMSS";
      this.mode.add(Integer.valueOf(this.combo.getSelectedIndex()));
      this.index = 0;
    }
    this.exit = false;
    this.ind = this.combo.getSelectedIndex();
    for (int i = this.index; i < this.list.size(); this.index += 1) {
      this.exit = false;
      String linija = (String)this.list.get(i);

      if (this.mode.isEmpty()) {
        if (linija.startsWith(this.line + "[" + this.ind + "]")) {
          int beginning = (this.line + "[" + this.ind + "]").length() + 1;
          int end = linija.indexOf("[", beginning);
          this.area.append("next move:  " + linija.substring(beginning, end));
          this.line = linija.substring(0, end);

          if (((String)this.list.get(i)).substring(((String)this.list.get(i)).indexOf("[", end), ((String)this.list.get(i)).indexOf("]", end)).contains("soln")) {
            this.area.append("  --->  SOLUTION!!\n\n");
            this.exit = true; break;
          }
          this.area.append("\n\n");

          break;
        }
      }
      else {
        search(1);
      }
      if (this.exit)
        break;
      i++;
    }
  }

  private void restartActionPerformed(ActionEvent evt)
  {
    this.line = "MMSS";
    this.area.setText("M - medal\n");
    this.area.append("S - scroll\n");
    this.area.append("P - potion\n");
    this.area.append("F - food\n\n");
    this.area.append("1st move is always MMSS!\n\n");
    this.index = 0;
    this.combo.setSelectedIndex(0);
  }

  private void readFile()
  {
    this.list = new ArrayList<String>();
    boolean start = false;
    BufferedReader in = null;
    try {
      in = new BufferedReader(new FileReader("mastermind.txt"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    String linija = "";
    while (true) {
      try {
        linija = in.readLine();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
      if (linija == null) {
        break;
      }
      if (start) {
        this.list.add(linija);
      }

      if (linija.startsWith("---"))
        start = true;
    }
  }

  /**
   * Searches for the next move.
   */
  private void search(int lvl)
  {
    if (this.exit)
      return;
    if (lvl > this.mode.size())
    {
      search(lvl + 1);
    }
    boolean found = false;

    for (int i = this.index; i < this.list.size(); this.index += 1)
    {
      if (((String)this.list.get(i)).startsWith(this.line + "[" + this.ind + "]")) {
        int beginning = (this.line + "[" + this.ind + "]").length() + 1;
        int end = ((String)this.list.get(i)).indexOf("[", beginning);
        this.area.append("next move:  " + ((String)this.list.get(i)).substring(beginning, end).replaceAll("\\.", "").replaceAll("]", ""));
        
        if (((String)this.list.get(i)).substring(((String)this.list.get(i)).indexOf("[", end), ((String)this.list.get(i)).indexOf("]", end)).contains("soln"))
          this.area.append("  --->  SOLUTION!!\n\n");
        else {
          this.area.append("\n\n");
        }

        this.line = ((String)this.list.get(i)).substring(0, end);

        found = true;
        break;
      }
      if (!((String)this.list.get(i)).startsWith(this.line))
        break;
      i++;
    }

    if (!found)
    {
      if (this.index >= this.list.size() - 1) {
    	this.area.append("No possible solutions!\n\n");
        this.exit = true;
        return;
      }

      boolean ok = true;
      int start = 0;

      for (int i = 0; i < this.mode.size(); i++) {
        String str = (String)this.list.get(this.index);
        start = str.indexOf("[", start) + 1;
        int end = str.indexOf("]", start);

        if (!str.substring(start, end).equals(this.mode.get(i) + "")) {
          ok = false;
          break;
        }
      }

      if (ok)
      {
        int beginning = this.line.lastIndexOf(".");
        int end = ((String)this.list.get(this.index)).indexOf("[", beginning);
        this.area.append("next move:  " + ((String)this.list.get(this.index)).substring(beginning, end).replaceAll("\\.", "").replaceAll("]", ""));
        if (((String)this.list.get(this.index)).substring(((String)this.list.get(this.index)).indexOf("[", end), ((String)this.list.get(this.index)).indexOf("]", end)).contains("soln"))
          this.area.append("  --->  SOLUTION!!\n\n");
        else {
          this.area.append("\n\n");
        }
        this.line = ((String)this.list.get(this.index)).substring(0, end);

        this.exit = true;
      }
    }
    else {
      this.exit = true;
    }
  }

  public static void main(String[] args)
  {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Mastermind().setVisible(true);
      }
    });
  }
}
