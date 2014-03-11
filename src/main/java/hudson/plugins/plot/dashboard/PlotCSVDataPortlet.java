package hudson.plugins.plot.dashboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.plugins.plot.Plot;
import hudson.plugins.view.dashboard.DashboardPortlet;
import hudson.util.Graph;

public class PlotCSVDataPortlet extends DashboardPortlet {

	Plot plot;

	private String name;

	private String jobName;

	private String group;

	private String title;

	private String numBuilds;

	private String yaxis;

	private String plotStyle;

	private String csvFileName;

	public String getName() {
		return name;
	}

	public String getJobName() {
		return jobName;
	}

	public String getGroup() {
		return group;
	}

	public String getTitle() {
		return title;
	}

	public String getNumBuilds() {
		return numBuilds;
	}

	public String getYaxis() {
		return yaxis;
	}

	public String getPlotStyle() {
		return plotStyle;
	}

	public String getCsvFileName() {
		return csvFileName;
	}

	@DataBoundConstructor
	public PlotCSVDataPortlet(String name, String group, String jobName,
			String title, String numBuilds, String yaxis, String plotStyle,
			String csvFileName) {

		super(name);
		this.name = name;
		this.jobName = jobName;
		this.group = group;
		this.title = title;
		this.numBuilds = numBuilds;
		this.yaxis = yaxis;
		this.plotStyle = plotStyle;
		this.csvFileName = csvFileName;

		createNewInstance();
	}

	public void createNewInstance() {
		plot = new Plot(this.title, this.yaxis, this.group, this.numBuilds,
				this.csvFileName, this.plotStyle, false);
		AbstractProject<?, ?> project = AbstractProject.findNearest(jobName);
		plot.setProject(project);
	}

	public Graph getSummaryGraph(StaplerRequest req) throws IOException {
		return plot.plotCsvData(req);
	}

	public List<String> dashBoardJobNames() {
		List<String> names = new ArrayList<String>();
		List<Job> jobs = getDashboard().getJobs();
		for (Job job : jobs) {
			names.add(job.getName());
		}
		return names;
	}

	@Extension(optional = true)
	public static class PlotBuildDataDescriptor extends
			Descriptor<DashboardPortlet> {
		@Override
		public String getDisplayName() {
			return "Plot CSV Data for regression builds";
		}
	}
}