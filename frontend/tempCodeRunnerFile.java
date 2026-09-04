public class Main {
    private int f(int[] arr, int[][] dp, int k, int n){
        if(k == 0){
            return 1;
        }
        if(n < 0 || k < 0)
            return 0;
        
        if(dp[n][k] != -1)
            return dp[n][k];

        int notPick = f(arr, dp, k, n - 1);
        int pick = f(arr, dp, k - arr[n], n - 1);

        return dp[n][k] = pick + notPick;

    }
    public int perfectSum(int[] arr, int K) {
        int n = arr.length;

        int[][] dp = new int[n][K + 1];
        for(int[] d: dp){
            Arrays.fill(d, -1); 
        }

        for(int i = 0; i < n; i++){

            if(K == 0 || arr[i] == K){
                dp[arr[i]][K] = 1;;
                continue;
            }
            else if(K < 0){
                dp[arr[i]][K] = 0;
                continue;
            }

            if(dp[arr[i]][K] != -1)
                continue;

            int notPick = i == 0 ? 0 : dp[arr[i - 1]][K];
            int pick = i == 0 || dp[arr[i - 1]][K - arr[i]] == -1 ? 0 : dp[arr[i - 1]][K - arr[i]];

            dp[arr[i]][K] = pick + notPick;
        }

        return dp[arr[n - 1]][K];

        // return f(arr, dp, K, n - 1);
    }

    // psvm
    public static void main(String[] args) {
        Main obj = new Main();
        int[] arr = {2, 3, 5, 16, 8, 10};
        int K = 10;
        System.out.println(obj.perfectSum(arr, K));
    }
}

