import java.util.Scanner;

public class Codeup1092 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
        int a = scan.nextInt();
        int b = scan.nextInt();
        int c = scan.nextInt();
        int day = 1;
        
        while(day%a!=0 || day%b!=0 || day%c!=0) 
        day++; 
        
        System.out.println(day);

	}

}
