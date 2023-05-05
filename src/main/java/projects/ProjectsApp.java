package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

public class ProjectsApp {

		private Scanner scanner = new Scanner(System.in);
		private ProjectService projectService = new ProjectService();
	 //@formatter:off
		private List<String> operations = List.of(
				"1) Add a project"
				);
	 //@formatter:on

	public static void main(String[] args) {
		new ProjectsApp().processUserSelections();
	
	}
	 // Process the user's menu selections and call the appropriate methods
	private void processUserSelections() {
boolean done = false;
		
		while(!done) {
			try {
				int selection = getUserSelection();
					switch(selection) {
						case -1:
							done = exitMenu();
							break;
						case 1:
							createProject();
							break;			
						default:
							System.out.println("\n" + selection + " is not valid. Try again.");
				}
			} catch (Exception e) {
				System.out.println("\nError" + e.toString() + " Try again.");
			}
		}
		
	}
	// Create a new project by getting user input and calling the ProjectService to save it
	private void createProject() {
		String projectName = getStringInput("Enter the project name");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
		String notes = getStringInput("Enter the project notes");
		
		Project project = new Project();
		
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);
		
		Project dbProject = projectService.addProject(project);
		System.out.println("You have successfully created project:\n" + dbProject);
		
	}
	// Get the user's menu selection
	private int getUserSelection() {
		printOperations();
		Integer input = getIntInput("\nEnter a menu selection");
		
		return Objects.isNull(input) ? -1 : input;
	}
	 // Print the available menu operations
	private void printOperations() {
		System.out.println("\nThese are the available selections. Press Enter to quit:");
		
		operations.forEach(line -> System.out.println("   " + line));
		
	}
	 // Get an integer input from the user
	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);
		
		if(Objects.isNull(input)) {
			return null;
		}
		
		try {
			return Integer.valueOf(input);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid number.");
		}

	}
	 // Get a string input from the user
	private String getStringInput(String prompt) {
			System.out.print(prompt + ": ");
			String line = scanner.nextLine();
			
			return line.isBlank() ? null : line.trim();
	}
	// Exit the menu and end the application
	private boolean exitMenu() {
		System.out.println("Exiting the menu...");
		return true;
	}
	// Get a decimal input from the user
	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);
		
		if(Objects.isNull(input)) {
			return null;
		}
		
		try {
			 // Attempt to create a BigDecimal object with the input, set its scale to 2 decimal places
			return new BigDecimal(input).setScale(2);
		} catch (NumberFormatException e) {
			  // If the input cannot be parsed as a BigDecimal, throw a DbException with an error message
			throw new DbException(input + " is not a valid number.");
		}
	}

}
