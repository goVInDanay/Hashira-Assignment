import org.json.JSONObject;
import java.math.BigInteger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HashiraSolver {
    public static void main(String[] args) {

        String testCase1Path = "testcase1.json";
        String testCase2Path = "testcase2.json";

        try {
            // --- Test Case 1 ---
            System.out.println("--- Test Case 1 ---");
            String testCase1Json = new String(Files.readAllBytes(Paths.get(testCase1Path)));
            BigInteger result1 = findConstantTerm(testCase1Json);
            System.out.println("Output (Constant Term 'c'): " + result1);
            System.out.println();

            // --- Test Case 2 ---
            System.out.println("--- Test Case 2 ---");
            String testCase2Json = new String(Files.readAllBytes(Paths.get(testCase2Path)));
            BigInteger result2 = findConstantTerm(testCase2Json);
            System.out.println("Output (Constant Term 'c'): " + result2);

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static BigInteger findConstantTerm(String jsonInput) {

        // --- Step 1: Parse the JSON input ---
        JSONObject obj = new JSONObject(jsonInput);
        int k = obj.getJSONObject("keys").getInt("k");

        // This will store our final sum, P(0) or 'c'
        BigInteger constantTermC = BigInteger.ZERO;

        // Loop from j = 1 to k (we only need k points)
        for (int j = 1; j <= k; j++) {

            // --- Step 2: Decode the y_j value ---
            JSONObject rootData = obj.getJSONObject(String.valueOf(j));
            int base = Integer.parseInt(rootData.getString("base"));
            String value = rootData.getString("value");

            // Decode y_j from its base into a BigInteger
            BigInteger y_j = new BigInteger(value, base);

            // --- Step 3: Use the formula to find 'c' ---
            // L_j(0) = (-1)^(j-1) * nCr(k, j)

            BigInteger nCr_k_j = nCr(k, j); // Call the correct helper

            BigInteger L_j_0;
            if (j % 2 == 0) {
                // j is even, so (j-1) is odd. Sign is negative.
                L_j_0 = nCr_k_j.negate();
            } else {
                // j is odd, so (j-1) is even. Sign is positive.
                L_j_0 = nCr_k_j;
            }

            // Add this term: y_j * L_j(0)
            constantTermC = constantTermC.add(y_j.multiply(L_j_0));
        }

        return constantTermC;
    }

    /**
     * Calculates "n choose r" (nCr) using BigInteger to avoid overflow.
     * This is the verified, correct implementation.
     */
    /**
     * Calculates "n choose r" (nCr) using BigInteger to avoid overflow.
     * This is the verified, correct implementation.
     */
    public static BigInteger nCr(int n, int r) {
        // Edge cases
        if (r < 0 || r > n) {
            return BigInteger.ZERO;
        }
        if (r == 0 || r == n) {
            return BigInteger.ONE;
        }

        // Optimization: nCr(n, r) == nCr(n, n-r)
        if (r > n / 2) {
            r = n - r;
        }

        // Formula: n * (n-1) * ... * (n-r+1) / r!
        // This calculation is safe and will not overflow.
        BigInteger result = BigInteger.ONE;
        for (int i = 1; i <= r; i++) {
            result = result.multiply(BigInteger.valueOf(n - i + 1))
                    .divide(BigInteger.valueOf(i));
        }
        return result;
    }
}
