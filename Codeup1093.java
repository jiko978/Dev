import java.util.Scanner;

public class Codeup1093 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
        String num = scan.nextLine();
        String str = scan.nextLine();
        String [] arr = str.split(" ");
        
        int [] total = new int[23];
        
        for(int i=0;i<Integer.parseInt(num);i++) {
        	//System.out.println(total[Integer.parseInt(arr[i])-1]);
        	total[Integer.parseInt(arr[i])-1] += 1;
        }
		
        for(int i=0;i<total.length;i++) {
        	System.out.format("%d ", total[i]);
        	//System.out.println(total[i] + " ");
        }

	}

}
