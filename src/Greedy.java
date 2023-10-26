import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Greedy {

	public static int[] GreedyFunction(int n, int p, int c, List<Integer> yearlyDemand, List<Integer> playerSalary) {
		int returnArr[] =new int[(n*3) + 2];
		int index = -2;
		int totalCost = 0;
		boolean flagNextYear1 = false;
		boolean flagNextYear2 = false;
		boolean flagControl = false;
		for(int i = 1; i <= n; i++)
		{
			index = index + 3;
			if(yearlyDemand.get(i - 1) > p) // i - 1 -> demand of current year (not the previous year)
			{
				if((flagNextYear1 == false) && (flagNextYear2 == false)) // p < demand for previous year
				{
					returnArr[index] = (yearlyDemand.get(i - 1) - p);
					returnArr[index + 1] = 0;
					returnArr[index + 2] = (yearlyDemand.get(i - 1) - p) * c;
					totalCost = totalCost + ((yearlyDemand.get(i - 1) - p) * c);
					returnArr[index + 3] = (i == n) ? totalCost: 0;
				}
				else {
					if(flagNextYear1 == true) // p > demand for previous year and p + nonRent > demand for current year
					{
						returnArr[index] = 0;
						returnArr[index + 1] = 0;
						returnArr[index + 2] = 0;
						returnArr[index + 3] = (i == n) ? totalCost: 0;
					}
					else if(flagNextYear2 == true) { // p > demand for previous year and p + nonRent < demand for current year
						returnArr[index] = yearlyDemand.get(i - 1) - p - returnArr[index - 2];
						returnArr[index + 1] = 0;
						returnArr[index + 2] = (yearlyDemand.get(i - 1) - p - returnArr[index - 2]) * c;
						totalCost = totalCost + ((yearlyDemand.get(i - 1) - p - returnArr[index - 2]) * c);
						returnArr[index + 3] = (i == n) ? totalCost: 0;
					}
				}
			}
			else if(yearlyDemand.get(i - 1) == p)
			{
				returnArr[index] = 0;
				returnArr[index + 1] = 0;
				returnArr[index + 2] = 0;
				returnArr[index + 3] = (i == n) ? totalCost: 0;
			}
			else {
				if(i != n)
				{
					if(yearlyDemand.get(i) > p) // demand > p for next year
					{
						if((p - yearlyDemand.get(i - 1) + p) >= yearlyDemand.get(i)) // if we have enough nonRent player for next year
						{
							flagNextYear1 = true;
							flagControl = true;
							returnArr[index] = 0;
							returnArr[index + 1] = (p - yearlyDemand.get(i - 1) + p - yearlyDemand.get(i) == 0) ? 1: (p - yearlyDemand.get(i - 1) + p - yearlyDemand.get(i));
							returnArr[index + 2] = ((p - yearlyDemand.get(i - 1) + p) == yearlyDemand.get(i)) ? 5: playerSalary.get(p - yearlyDemand.get(i - 1) + p - yearlyDemand.get(i) - 1);
							totalCost = totalCost + returnArr[index + 2];
						}
						else { // if we have not enough nonRent player for next year
							flagNextYear2 = true;
							flagControl = true;
							returnArr[index] = 0;
							returnArr[index + 1] = p - yearlyDemand.get(i - 1);
							returnArr[index + 2] = playerSalary.get(p - yearlyDemand.get(i - 1) - 1);
							totalCost = totalCost + playerSalary.get(p - yearlyDemand.get(i - 1) - 1);
						}
					}
					else {
						returnArr[index] = 0;
						returnArr[index + 1] = 0;
						returnArr[index + 2] = 0;
						returnArr[index + 3] = (i == n) ? totalCost: 0;
					}
				}
				else {
					returnArr[index] = 0;
					returnArr[index + 1] = 0;
					returnArr[index + 2] = 0;
					returnArr[index + 3] = (i == n) ? totalCost: 0;
				}
			}
			if(flagControl == false)
			{
				flagNextYear1 = false;
				flagNextYear2 = false;
			}
			flagControl = false;
		}
		return returnArr;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		 int n = 10, p = 5, c = 10;
	        
	        List<Integer> yearlyDemand = new ArrayList<>();
	        List<Integer> playerSalary = new ArrayList<>();
	        int count = 1;
	        
	        try (BufferedReader br = new BufferedReader(new FileReader("yearly_player_demand.txt"))) {
	            String line;
	            br.readLine();
	            while ((line = br.readLine()) != null) {
	                String[] values = line.split("\\s+");
	                yearlyDemand.add(Integer.parseInt(values[1]));
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        try (BufferedReader br = new BufferedReader(new FileReader("players_salary.txt"))) {
	            String line;
	            br.readLine();
	            while ((line = br.readLine()) != null) {
	                String[] values = line.split("\\s+");
	                playerSalary.add(Integer.parseInt(values[1]));
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        int[] result = GreedyFunction(n, p, c, yearlyDemand, playerSalary);
	        int total = 0;
	        for(int i = 1; i <= (3 * n) + 1; i++)
	        {
	        	if((3 * n) + 1 == i)
	        	{
	        		System.out.println(result[i]);
	        		break;
	        	}
	        	total = result[i + 2] + total;
	        	System.out.print("year" + count + " " + "Demand: " + yearlyDemand.get(count - 1) + " --> " + result[i] + " coaches, " + result[i + 1] + " nonRent Player, cost: " + result[i + 2] + ", total cost: " + total);
	        	if((result[i + 1] != 0) && (result[i - 2] != 0))
	        		System.out.print(" *** +nonRent Player to previous year ***");
	        	System.out.println();
	        	count++;
	        	i = i + 2;
	        	System.out.println();
	        }
	}

}
