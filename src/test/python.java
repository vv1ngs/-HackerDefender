import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author vvings
 * @version 2020/5/11 21:22
 */
public class python {
    public static void main(String[] args) {
        String[] cmd = new String[]{"C:\\python3\\python3.exe", "C:\\Users\\22080\\Desktop\\1.py", "1.py"};
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader er = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String output = null;
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            while ((output = er.readLine()) != null) {
                System.out.println(output);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
