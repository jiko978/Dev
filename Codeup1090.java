import java.util.*;

public class Codeup1090 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        Scanner scan = new Scanner(System.in);

        int a = scan.nextInt();
        int b = scan.nextInt();
        int c = scan.nextInt();
        //int result = 0;
        
        for(int i=1;i<c;i++){
            a *= b;    
        }
        
        System.out.println(a);
	}

}
