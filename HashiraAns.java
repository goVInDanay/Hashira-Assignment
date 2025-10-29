import org.json.JSONObject;
import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.*;

public class PolynomialInterpolation {
    
    // Function to decode base-specific values into integers
    public static BigInteger decodeValue(String value, int base) {
        return new BigInteger(value, base);
    }

    // Lagrange interpolation to find the polynomial at x = 0
    public static BigInteger lagrangeInterpolation(List<int[]> roots) {
        BigInteger result = BigInteger.ZERO;
        for (int i = 0; i < roots.size(); i++) {
            int[] root = roots.get(i);
            BigInteger xi = BigInteger.valueOf(root[0]);
            BigInteger yi = BigInteger.valueOf(root[1]);
            
            BigInteger term = yi;
            for (int j = 0; j < roots.size(); j++) {
                if (i != j) {
                    int[] otherRoot = roots.get(j);
                    BigInteger xj = BigInteger.valueOf(otherRoot[0]);
                    term = term.multiply(BigInteger.ZERO.subtract(xj)).divide(xi.subtract(xj));
                }
            }
            result = result.add(term);
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        // Load JSON from file
        JSONObject json = new JSONObject(new FileReader("testcase1.json"));

        // Get the n, k values from the JSON
        JSONObject keys = json.getJSONObject("keys");
        int n = keys.getInt("n");
        int k = keys.getInt("k");

        // Parse the roots (x, y) pairs from the JSON
        List<int[]> roots = new ArrayList<>();
        for (String key : json.keySet()) {
            if (!key.equals("keys")) {
                JSONObject rootData = json.getJSONObject(key);
                int base = Integer.parseInt(rootData.getString("base"));
                String value = rootData.getString("value");
                int x = Integer.parseInt(key);
                int y = decodeValue(value, base).intValue();
                roots.add(new int[]{x, y});
            }
        }

        // Step 2: Apply Lagrange Interpolation to get the constant term (c)
        BigInteger constantTerm = lagrangeInterpolation(roots);
        System.out.println("Constant term (c) for Test Case 1: " + constantTerm);

        // Repeat for Test Case 2 (testcase2.json)
        json = new JSONObject(new FileReader("testcase2.json"));
        keys = json.getJSONObject("keys");
        n = keys.getInt("n");
        k = keys.getInt("k");

        roots.clear();
        for (String key : json.keySet()) {
            if (!key.equals("keys")) {
                JSONObject rootData = json.getJSONObject(key);
                int base = Integer.parseInt(rootData.getString("base"));
                String value = rootData.getString("value");
                int x = Integer.parseInt(key);
                int y = decodeValue(value, base).intValue();
                roots.add(new int[]{x, y});
            }
        }

        // Step 2: Apply Lagrange Interpolation to get the constant term (c) for Test Case 2
        constantTerm = lagrangeInterpolation(roots);
        System.out.println("Constant term (c) for Test Case 2: " + constantTerm);
    }
}
