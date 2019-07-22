import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class bookmarking {

  public static void main(String x[]) {
    String FileName = "src/main/resources/messages_v2.txt";
    try {
      ArrayList<JSONObject> jsons = ReadJSON(new File(FileName), "UTF-8");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  public static synchronized ArrayList<JSONObject> ReadJSON(File MyFile, String Encoding)
      throws FileNotFoundException, ParseException {
    Scanner scn = new Scanner(MyFile, Encoding);
    ArrayList<JSONObject> json = new ArrayList<JSONObject>();
    ArrayList<JSONObject> betJson = new ArrayList<JSONObject>();
    Table<String, String, Double> table = HashBasedTable.create();

    while (scn.hasNext()) {
      JSONObject obj = (JSONObject) new JSONParser().parse(scn.nextLine());
      json.add(obj);
    }

    for (JSONObject obj : json) {

      JSONObject objBet = (JSONObject) obj.get("bet");
      String fixture = (String) objBet.get("fixture");
      String outcome = (String) objBet.get("outcome");
      Double stake = (Double) objBet.get("stake");

      if (obj.get("type").equals("BET")) {
        betJson.add(objBet);

        if (table.get(fixture, "X") == null) {
          table.put(fixture, "X", 0.00);
        }
        if (table.get(fixture, "1") == null) {
          table.put(fixture, "1", 0.00);
        }
        if (table.get(fixture, "2") == null) {
          table.put(fixture, "2", 0.00);
        }

        if (table.get(fixture, outcome) > 0) {
          table.put(fixture, outcome, stake + (table.get(fixture, outcome)));
        } else {

          table.put(fixture, outcome, stake);
        }
      }

      if (table.get(fixture, "X") == null) {
        table.put(fixture, "X", 0.00);
      }
      if (table.get(fixture, "1") == null) {
        table.put(fixture, "1", 0.00);
      }
      if (table.get(fixture, "2") == null) {
        table.put(fixture, "2", 0.00);
      }

      System.out.println(
          fixture + " 1: " + table.get(fixture, "1") + " X: " + table.get(fixture, "X") + " 2: "
              + table.get(fixture, "2"));

    }
    return json;
  }

}