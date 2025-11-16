import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Judge Bias (-1 to +1):");
        double judgeBias = sc.nextDouble();

        System.out.println("System Fairness (0 to 1):");
        double fairness = sc.nextDouble();

        System.out.println("Base Guilt Probability (0 to 1):");
        double baseGuilt = sc.nextDouble();

        System.out.println("Strength: Forensic Evidence (0 to 1):");
        double forensic = sc.nextDouble();

        System.out.println("Strength: Witness Evidence (0 to 1):");
        double witness = sc.nextDouble();

        System.out.println("Strength: Digital Evidence (0 to 1):");
        double digital = sc.nextDouble();

        System.out.println("Number of cases to simulate:");
        int cases = sc.nextInt();

        Random rand = new Random();
        int convictions = 0;
        int wrongfulConvictions = 0;
        int correctAcquittals = 0;

        for (int i = 0; i < cases; i++) {
            boolean actuallyGuilty = rand.nextDouble() < baseGuilt;

            double combinedEvidence = (
                    forensic * 0.45 +
                    witness * 0.30 +
                    digital * 0.25
            );

            double likelihoodGuilty = combinedEvidence * (0.6 + 0.4 * fairness);
            double likelihoodInnocent = (1 - combinedEvidence) * (0.4 + 0.6 * (1 - fairness));

            double posterior = (likelihoodGuilty * baseGuilt) /
                    ((likelihoodGuilty * baseGuilt) + (likelihoodInnocent * (1 - baseGuilt)));

            posterior = posterior + judgeBias * 0.15;
            posterior = Math.max(0, Math.min(1, posterior));

            boolean convict = posterior > 0.5;

            if (convict) {
                convictions++;
                if (!actuallyGuilty) wrongfulConvictions++;
            } else {
                if (!actuallyGuilty) correctAcquittals++;
            }
        }

        double convictionRate = (double) convictions / cases;
        double wrongfulRate = (double) wrongfulConvictions / cases;
        double fairnessIndex = Math.max(0, 1 - (wrongfulRate * 3));

        System.out.println("\n=== LEGAL DECISION ANALYSIS ===");
        System.out.println("Conviction Rate: " + convictionRate);
        System.out.println("Wrongful Conviction Rate: " + wrongfulRate);
        System.out.println("Correct Acquittal Rate: " + ((double) correctAcquittals / cases));
        System.out.println("Fairness Index (0–1): " + fairnessIndex);

        if (wrongfulRate > 0.20) {
            System.out.println("System Status: CRITICALLY UNJUST");
        } else if (wrongfulRate > 0.10) {
            System.out.println("System Status: STRUCTURALLY BIASED");
        } else if (wrongfulRate > 0.05) {
            System.out.println("System Status: PARTIALLY FAIR");
        } else {
            System.out.println("System Status: GENERALLY FAIR");
        }
    }
}
