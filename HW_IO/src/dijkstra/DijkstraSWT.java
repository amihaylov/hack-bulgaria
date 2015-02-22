package dijkstra;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.List;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class DijkstraSWT extends Composite {
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;

	private java.util.List<Vertex> nodes;
	private java.util.List<Edge> edges;
	private int distance = 0;
	private int nodesCount = 0;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public DijkstraSWT(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(6, false));

		Label lblCity = new Label(this, SWT.NONE);
		lblCity.setText("City");
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		Label lblListWithCities = new Label(this, SWT.NONE);
		lblListWithCities.setText("List with cities");

		text = new Text(this, SWT.BORDER);
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text.widthHint = 66;
		text.setLayoutData(gd_text);
		new Label(this, SWT.NONE);

		Button btnAddCity = new Button(this, SWT.NONE);
		btnAddCity.setText("Add City");
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		List list = new List(this, SWT.BORDER);
		GridData gd_list = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
				1);
		gd_list.widthHint = 116;
		list.setLayoutData(gd_list);

		btnAddCity.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (lblCity.getText() != "") {
					Vertex location = new Vertex(Integer.toString(nodesCount),
							"Node_" + nodesCount);
					nodesCount++;
					nodes.add(location);
					list.add(lblCity.getText());
				} else
					JOptionPane.showMessageDialog(null, "City cannot be empty",
							"Error!", JOptionPane.ERROR_MESSAGE);
			}
		});

		Label lblSourceCity = new Label(this, SWT.NONE);
		lblSourceCity.setText("Source city");
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		Label lblListWithSegments = new Label(this, SWT.NONE);
		lblListWithSegments.setText("List with segments");

		text_1 = new Text(this, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));
		new Label(this, SWT.NONE);

		Button btnAdd = new Button(this, SWT.NONE);

		btnAdd.setToolTipText("Add a track between source and destination city to the list of segments.");
		btnAdd.setText("Add Segment");
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		List list_1 = new List(this, SWT.BORDER);
		GridData gd_list_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 3);
		gd_list_1.widthHint = 124;
		list_1.setLayoutData(gd_list_1);

		Label lblDestinationCity = new Label(this, SWT.NONE);
		lblDestinationCity.setText("Destination city");
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		text_2 = new Text(this, SWT.BORDER);
		text_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));
		new Label(this, SWT.NONE);

		Button btnCalculate = new Button(this, SWT.NONE);
		btnCalculate
				.setToolTipText("Calculate the distance between Source and Destination city");
		btnCalculate.setText("Calculate");
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		Label lblDistance = new Label(this, SWT.NONE);
		lblDistance.setText("Distance");
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				//Possible bug on isNumeric, check if int passes, also addLane should be checked
				if ((lblSourceCity.getText() != "")
						&& (lblDestinationCity.getText() != "")
						&& (lblDistance.getText() != "")
						&& (isNumeric(lblDistance.getText()))) {
					//TODO Look at the line below, must compare textfields with existing
					//cities and if they both exist, must extract sourceLocNo and destLocNo
					//and use them to addLane. Check also the TestDijkstra.java
					//addLane(lblSourceCity.getText() + " to " + lblDestinationCity.getText(), 0, 1, 85);
				}
			}
		});

		text_3 = new Text(this, SWT.BORDER);
		text_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public static boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	 private void addLane(String laneId, int sourceLocNo, int destLocNo,
		      int duration) {
		    Edge lane = new Edge(laneId,nodes.get(sourceLocNo), nodes.get(destLocNo), duration);
		    edges.add(lane);
		  }
}
