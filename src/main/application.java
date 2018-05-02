/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import entities.GasData;

public class application {
	public static void main(String[] args) {
		System.out.println("Starting Program.");
		// Start Time
		ZonedDateTime startTime = ZonedDateTime.now();

		// Initialize the List
		List<GasData> allData = new ArrayList<GasData>();

		// Read in the file
		System.out.println("Reading input file.");
		try {
			FileReader dataFile = new FileReader("src/resources/gas-data.txt");
			allData = digestFile(dataFile);
		} catch (Exception e) {
			System.out.println("[ERROR] Exception while reading file");
			e.printStackTrace();
		}
		System.out.println("Finished reading input file.");

		// Convert to csv file
		System.out.println("Converting to CSV.");
		String csvFile = "gas-data.csv";
		FileWriter writer;
		try {
			writer = new FileWriter(csvFile);
			writeFile(writer, allData);
		} catch (IOException e) {
			System.out.println("[ERROR] Exception while writing CSV File");
			e.printStackTrace();
		}
		System.out.println("Finished writing output file.");

		// End Time
		ZonedDateTime endTime = ZonedDateTime.now();
		System.out.println("Finished. Job Time: " + Duration.between(startTime, endTime));
	}

	private static List<GasData> digestFile(FileReader dataFile) {
		BufferedReader in = new BufferedReader(dataFile);
		List<GasData> allLocalData = new ArrayList<GasData>();

		try {

			String currentLine;
			// iterate through the whole file
			while ((currentLine = in.readLine()) != null) {

				// Ensures an extra blank line does not break the program
				if (currentLine.isEmpty()) {
					System.out.println(
							"[WARN ] We detected an extra blank line. We corrected the issue but please correct your mistake.");
					continue;
				}

				// Initialize Object
				GasData data = new GasData();

				// Set all the Object's Properties
				data.setDate(parseDate(currentLine));

				currentLine = in.readLine();
				data.setMoney(parseMoney(currentLine));

				currentLine = in.readLine();
				data.setGallons(parseGallons(currentLine));

				currentLine = in.readLine();
				data.setMileage(parseMileage(currentLine));

				// If the line "NOT FULL" is present
				// The tank was not full (initialized as true - set to false)
				currentLine = in.readLine();
				if (currentLine.equals("NOT FULL")) {
					data.setFull(false);
					currentLine = in.readLine(); // Eat the following empty line
				}

				System.out.println("Read Data from: " + data.getDate());

				allLocalData.add(data);
			}

		} catch (Exception e) {
			System.out.println("[ERROR] Exception while parsing data");
			e.printStackTrace();
		}
		return allLocalData;
	}

	private static String parseDate(String currentLine) {
		String[] split = currentLine.split("\\s+");
		String dateString = currentLine;

		// parse year
		try {
			dateString = split[2];
		} catch (Exception e) {
			System.out.println("[ERROR] Your Date is not formatted correctly. Please resolve.");
			dateString = "0000";
		}

		// Add Seperator
		dateString += "-";

		// parse month
		if (split[0].equals("January")) {
			dateString += "01";
		} else if (split[0].equals("February")) {
			dateString += "02";
		} else if (split[0].equals("March")) {
			dateString += "03";
		} else if (split[0].equals("April")) {
			dateString += "04";
		} else if (split[0].equals("May")) {
			dateString += "05";
		} else if (split[0].equals("June")) {
			dateString += "06";
		} else if (split[0].equals("July")) {
			dateString += "07";
		} else if (split[0].equals("August")) {
			dateString += "08";
		} else if (split[0].equals("September")) {
			dateString += "09";
		} else if (split[0].equals("October")) {
			dateString += "10";
		} else if (split[0].equals("November")) {
			dateString += "11";
		} else if (split[0].equals("December")) {
			dateString += "12";
		} else {
			System.out.println("[ERROR] Error parsing Month: " + split[0]);
			dateString += "00";
		}

		// Add Seperator
		dateString += "-";

		// parse day
		try {
			String dayNumber = split[1];
			// Remove comma
			dayNumber = dayNumber.substring(0, dayNumber.length() - 1);
			// Ensure proper length
			if (dayNumber.length() == 1) {
				dayNumber = "0" + dayNumber;
			}
			dateString += dayNumber;
		} catch (Exception e) {
			System.out.println("[ERROR] Your Date is not formatted correctly. Please resolve.");
			dateString += "00";
		}
		return dateString;
	}

	private static double parseMoney(String currentLine) {
		String moneyString = currentLine.substring(1);
		Double money = Double.parseDouble(moneyString);
		return money;
	}

	private static double parseGallons(String currentLine) {
		String gallonString = currentLine.substring(0, currentLine.indexOf(" "));
		Double gallons = Double.parseDouble(gallonString);
		return gallons;
	}

	private static int parseMileage(String currentLine) {
		try {
			String mileageString = currentLine.substring(0, currentLine.indexOf(" "));
			double mileageAsDouble = Double.parseDouble(mileageString);
			Integer mileage = -1;

			// round to the nearest whole number
			if (mileageAsDouble % 1 < 5) {
				mileage = (int) mileageAsDouble;
			} else {
				mileage = (int) mileageAsDouble + 1;
			}

			return mileage;
		} catch (Exception e) {
			System.out.println("[ERROR] Improper format of input file. Please check your input.");
			e.printStackTrace();

			return -1;
		}
	}

	private static void writeFile(FileWriter writer, List<GasData> allData) {
		try {
			System.out.println("Creating CSV file.");

			// Set up Header
			writer.append("Date");
			writer.append(",");
			writer.append("Price");
			writer.append(",");
			writer.append("Fuel (us gallons)");
			writer.append(",");
			writer.append("Mileage");
			writer.append(",");
			writer.append("Full Tank");
			writer.append("\n");

			// Populate file
			System.out.println("Populating CSV file.");
			for (GasData data : allData) {
				writer.append(data.getDate());
				writer.append(",");
				writer.append(Double.toString(data.getMoney()));
				writer.append(",");
				writer.append(Double.toString(data.getGallons()));
				writer.append(",");
				writer.append(Double.toString(data.getMileage()));
				writer.append(",");
				writer.append(Integer.toString(data.isFull() ? 1 : 0));
				writer.append("\n");
			}

			writer.flush();
			writer.close();

			System.out.println("CSV file was successfully created.");

		} catch (Exception e) {
			System.out.println("[ERROR] Exception while writing the CSV file.");
			e.printStackTrace();
		}

	}

}
