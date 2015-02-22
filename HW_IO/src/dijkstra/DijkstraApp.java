package dijkstra;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;








import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
public class DijkstraApp extends JFrame {

	private JPanel contentPane;
	private JTextField tfCity;
	private JTextField tfSource;
	private JTextField tfDestination;
	private JTextField tfDistance;
	
	private List<Vertex> nodes = new ArrayList<Vertex>();
	private List<Edge> edges = new ArrayList<Edge>();
	private int distance = 0;
	private int nodesCount = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DijkstraApp frame = new DijkstraApp();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DijkstraApp() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 455, 397);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JButton btnAddCity = new JButton("Add City");

		tfCity = new JTextField();
		tfCity.setColumns(10);
		
		JLabel lblCity = new JLabel("City");
		
		tfSource = new JTextField();
		tfSource.setColumns(10);
		
		JLabel lblSource = new JLabel("Source");
		
		tfDestination = new JTextField();
		tfDestination.setColumns(10);
		
		JLabel lblDestination = new JLabel("Destination");
		
		tfDistance = new JTextField();
		tfDistance.setColumns(10);
		
		JLabel lblDistance = new JLabel("Distance (closest if calculate)");
		
		JButton btnAddSegment = new JButton("Add Segment");
		
		JButton btnCalculate = new JButton("Calculate");
		
		JTextArea txtrCitiesList = new JTextArea();
		txtrCitiesList.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(txtrCitiesList);
		
		JLabel lblListWithCities = new JLabel("List with cities");
		
		JTextArea txtrSegments = new JTextArea();
		txtrSegments.setEditable(false);
		JScrollPane scrollPane2 = new JScrollPane(txtrSegments);
		
		JLabel lblSegments = new JLabel("Segments");
		
		JTextArea txtrShortestPath = new JTextArea();
		txtrShortestPath.setEditable(true);
		JScrollPane scrollPane3 = new JScrollPane(txtrShortestPath);
		
		//TODO Make check not to duplicate city if already exists, current doesnt work
		btnAddCity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!tfCity.getText().isEmpty()) {
					Vertex location = new Vertex(Integer.toString(nodesCount),
							tfCity.getText());
					if (!txtrCitiesList.toString().contains(tfCity.getText())) {
						nodesCount++;
						nodes.add(location);
						txtrCitiesList.append(tfCity.getText() + "\n");
					}
					else
						JOptionPane.showMessageDialog(null, "City already exists." + tfCity.getText(),
								"Error!", JOptionPane.ERROR_MESSAGE);	
				} else
					JOptionPane.showMessageDialog(null, "City cannot be empty." + tfCity.getText(),
							"Error!", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		/** Added reverse statement - now adding X to Y segment
		 * 	also adds Y to X segment. 
		 */
		btnAddSegment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Possible bug on isNumeric, check if int passes, also addLane
				// should be checked
				if (!(tfSource.getText().equals(""))
						&& !(tfDestination.getText().equals(""))
						&& !(tfDistance.getText().equals(""))
						&& (isInt(tfDistance.getText()))) {
					boolean sourceExists = false, destExists = false;
					int sourceLocNo = 0, destLocNo = 0;
					for (int i = 0; i < nodes.size(); i++) {
						if (nodes.get(i).getName()
								.equals(tfSource.getText())) {
							sourceExists = true;
							sourceLocNo = i;
						}
						if (nodes.get(i).getName()
								.equals(tfDestination.getText())) {
							destExists = true;
							destLocNo = i;
						}
					}
					if (sourceExists && destExists
							&& (sourceLocNo != destLocNo)) {
						addLane(tfSource.getText() + " to "
								+ tfDestination.getText(), sourceLocNo,
								destLocNo,
								Integer.parseInt(tfDistance.getText()));
						txtrSegments.append(tfSource.getText() + " to "
								+ tfDestination.getText() + " " + tfDistance.getText() + "\n");
						
						addLane(tfDestination.getText() + " to "
								+ tfSource.getText(), destLocNo,
								sourceLocNo,
								Integer.parseInt(tfDistance.getText()));
						txtrSegments.append(tfDestination.getText() + " to "
								+ tfSource.getText() + " " + tfDistance.getText() + "\n");
					}
					
					else
					{
						JOptionPane.showMessageDialog(null,
								"Source and/or Destination Cities do not exist, enter them first. "
										+ "Also source and destination cities should not be the same.",
								"Error!", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null,
							"Source and Destination Cities cannot be empty,"
									+ " distance should be an integer.",
							"Error!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		
		btnCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				boolean sourceExists = false, destExists = false;
				int sourceLocNo = 0, destLocNo = 0;
				
				for (int i = 0; i < nodes.size(); i++) {
					if (nodes.get(i).getName()
							.equals(tfSource.getText())) {
						sourceExists = true;
						sourceLocNo = i;
					}
					if (nodes.get(i).getName()
							.equals(tfDestination.getText())) {
						destExists = true;
						destLocNo = i;
					}
				}
				//Checking if source and dest exist or are the same and executing if they exist and are different
				if (sourceExists && destExists && (sourceLocNo!=destLocNo)) {
				    Graph graph = new Graph(nodes, edges);
				    DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
				    dijkstra.execute(nodes.get(sourceLocNo));
				    LinkedList<Vertex> path = dijkstra.getPath(nodes.get(destLocNo));
				    
				    //Finding the shortest path distance
				    for (int i=0; i<path.size()-1;i++) {
				    	String source = path.get(i).getId();
				    	String dest = path.get(i+1).getId();
				    	for (int j=0; j<edges.size();j++) {
				    		if((edges.get(j).getSource().getId()==source)&&(edges.get(j).getDestination().getId()==dest)) {
				    			distance+=edges.get(j).getWeight();
				    		}
				    	}
				    }
				    
				    for (Vertex vertex : path) {
				        txtrShortestPath.append(vertex.toString()+"\n");;
				      }
				    txtrShortestPath.append(Integer.toString(distance)+"\n");
				    distance=0;
				}
				else {
					JOptionPane.showMessageDialog(null,
							"Source and/or Destination Cities do not exist, enter them first. "
									+ "Also source and destination cities should not be the same.",
							"Error!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		JLabel lblShortestPath = new JLabel("Shortest Path");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblCity)
								.addComponent(lblSource)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_contentPane.createSequentialGroup()
													.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
														.addComponent(tfCity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(tfSource, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
													.addGap(50)
													.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
														.addComponent(btnAddCity)
														.addComponent(btnAddSegment)))
												.addComponent(lblDestination))
											.addGap(36))
										.addGroup(gl_contentPane.createSequentialGroup()
											.addComponent(tfDistance, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
											.addComponent(btnCalculate)
											.addGap(50)))
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
											.addComponent(lblListWithCities)
											.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE))
										.addComponent(lblSegments)
										.addComponent(lblShortestPath)
										.addComponent(scrollPane3, GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))))
							.addGap(47))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(tfDestination, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(333, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblDistance)
							.addContainerGap(280, Short.MAX_VALUE))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCity)
						.addComponent(lblListWithCities))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAddCity)
						.addComponent(tfCity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE))
					.addGap(20)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSource)
						.addComponent(lblSegments))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(tfSource, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(14)
							.addComponent(lblDestination)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfDestination, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnAddSegment)
						.addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblDistance)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(tfDistance, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnCalculate)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(21)
							.addComponent(lblShortestPath)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(scrollPane3, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)))
					.addGap(46))
		);
		contentPane.setLayout(gl_contentPane);
	}
	public static boolean isInt(String str) {
		try {
			int d = Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	private void addLane(String laneId, int sourceLocNo, int destLocNo,
			int duration) {
		Edge lane = new Edge(laneId, nodes.get(sourceLocNo),
				nodes.get(destLocNo), duration);
		edges.add(lane);
	}
	
}
