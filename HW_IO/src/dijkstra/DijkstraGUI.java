package dijkstra;

import javax.swing.JOptionPane;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import java.util.ArrayList;
import java.util.LinkedList;

public class DijkstraGUI {

	protected Shell shell;
	private static Text cityField;
	private Text sourceCityField;
	private Text destinationCityField;
	private Text distanceField;

	private java.util.List<Vertex> nodes;
	private java.util.List<Edge> edges;
	private int distance = 0;
	private int nodesCount = 0;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DijkstraGUI window = new DijkstraGUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 383);
		shell.setText("SWT Application");
		shell.setLayout(new GridLayout(5, false));
		new Label(shell, SWT.NONE);

		Label lblCity = new Label(shell, SWT.NONE);
		lblCity.setText("City");
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);

		Label lblListWithCities = new Label(shell, SWT.NONE);
		lblListWithCities.setText("List with cities");
		new Label(shell, SWT.NONE);

		cityField = new Text(shell, SWT.BORDER);
		cityField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		Button btnAddCity = new Button(shell, SWT.NONE);
		btnAddCity
				.setToolTipText("Add a track between source and destination city to the list of segments.");
		btnAddCity.setText("Add City");
		new Label(shell, SWT.NONE);

		List cityList = new List(shell, SWT.BORDER);
		GridData gd_cityList = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 1);
		gd_cityList.widthHint = 120;
		cityList.setLayoutData(gd_cityList);
		new Label(shell, SWT.NONE);

		Label lblSourceCity = new Label(shell, SWT.NONE);
		lblSourceCity.setText("Source city");
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);

		Label lblListWithSegments = new Label(shell, SWT.NONE);
		lblListWithSegments.setText("List with Segments");
		new Label(shell, SWT.NONE);

		sourceCityField = new Text(shell, SWT.BORDER);
		sourceCityField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Button btnAddSegment = new Button(shell, SWT.NONE);
		btnAddSegment
				.setToolTipText("Add segment by filling source and destination cities and distance between them.");
		btnAddSegment.setText("Add Segment");
		new Label(shell, SWT.NONE);

		List segmentList = new List(shell, SWT.BORDER);
		GridData gd_segmentList = new GridData(SWT.LEFT, SWT.TOP, false, false,
				1, 4);
		gd_segmentList.heightHint = 94;
		gd_segmentList.widthHint = 119;
		segmentList.setLayoutData(gd_segmentList);
		new Label(shell, SWT.NONE);

		Label lblDestinationCity = new Label(shell, SWT.NONE);
		lblDestinationCity.setText("Destination city");
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);

		destinationCityField = new Text(shell, SWT.BORDER);
		destinationCityField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		Button btnCalculate = new Button(shell, SWT.NONE);
		btnCalculate
				.setToolTipText("Calculate the distance between Source and Destination city.");
		btnCalculate.setText("Calculate");
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);

		Label lblDistance = new Label(shell, SWT.NONE);
		lblDistance.setText("Distance (closest if Calculate)");
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);

		distanceField = new Text(shell, SWT.BORDER);
		distanceField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);

		Label lblClosestDistancePath = new Label(shell, SWT.NONE);
		lblClosestDistancePath.setText("Closest distance path");
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);

		List closestPathList = new List(shell, SWT.BORDER);
		GridData gd_closestPathList = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_closestPathList.widthHint = 152;
		closestPathList.setLayoutData(gd_closestPathList);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);

		btnAddCity.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String temp = cityField.getText();
				System.out.println(temp);
				if (temp.equals("")) {
					Vertex location = new Vertex(Integer.toString(nodesCount),
							cityField.getText());
					nodesCount++;
					nodes.add(location);
					cityList.add(cityField.getText());
				} else
					JOptionPane.showMessageDialog(null, "City cannot be empty." + cityField.getText(),
							"Error!", JOptionPane.ERROR_MESSAGE);
			}
			
		});

		btnAddSegment.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// Possible bug on isNumeric, check if int passes, also addLane
				// should be checked
				if ((sourceCityField.getText().equals(""))
						&& (destinationCityField.getText().equals(""))
						&& (distanceField.getText().equals(""))
						&& (isInt(distanceField.getText()))) {
					boolean sourceExists = false, destExists = false;
					int sourceLocNo = 0, destLocNo = 0;
					// TODO Look at the line below, must compare textfields with
					// existing
					// cities and if they both exist, must extract sourceLocNo
					// and destLocNo
					// and use them to addLane. Check also the TestDijkstra.java
					for (int i = 0; i < nodes.size(); i++) {
						if (nodes.get(i).getName()
								.equals(sourceCityField.getText())) {
							sourceExists = true;
							sourceLocNo = i;
						}
						if (nodes.get(i).getName()
								.equals(destinationCityField.getText())) {
							destExists = true;
							destLocNo = i;
						}
					}
					if (sourceExists && destExists
							&& (sourceLocNo != destLocNo)) {
						addLane(sourceCityField.getText() + " to "
								+ destinationCityField.getText(), sourceLocNo,
								destLocNo,
								Integer.parseInt(distanceField.getText()));
						segmentList.add(sourceCityField.getText() + " to "
								+ destinationCityField.getText() + " " + distanceField.getText());
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
