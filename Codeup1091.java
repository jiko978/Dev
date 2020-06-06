import java.util.*;

public class Codeup1091 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);

		int a = scan.nextInt();
        int b = scan.nextInt();
        int c = scan.nextInt();
        int d = scan.nextInt();
        scan.close();

        for(int i=1;i<d;i++){
            a = (a * b) + c;    
        }
        
        System.out.println(a);        

	}

}
