/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package major_;

/**
 *
 * @author priyankarora
 */
public class optimal {
    
}
/*
    public void optimalWeight() {
        logicWeight = new double[sentenceCount];
        double sumtkj[] = new double[sentenceCount];//sum of weight of words in sentence k
        double modsk[] = new double[sentenceCount]; //length of sentence
        double LC[] = new double[sentenceCount];    //weight factor as per position in paragraph
        double CC[] = new double[sentenceCount];                                 //weight factor of indicating words in sentence
        double EC[] = new double[sentenceCount];
        //defining  important phrases in paper
        double pi[] = new double[sentenceCount];      //number of important words in sentence
        double q[] = new double[sentenceCount];       //total number of words in sentence

        for (int t = 0; t < sentenceCount; t++) {
            for (int k = 0; k < wordCount; k++) {
                sumtkj[t] += weight[k][t];
            }
            int count = 0;
            for (int k = 0; k < wordCount; k++) {
                if (weight[k][t] != 0) {
                    count++;
                }
            }
            modsk[t] = count;
            q[t] = count;
            count = 0;
            String str = sentenceDocArray.get(t);
            int l = impWords.length;
            int count1 = 0;
            for (int k = 0; k < l; k++) {
                if (str.contains(impWords[l])) {
                    CC[t] = 1.5;
                    count1++;
                }
            }
            pi[t] = count1;
            count1 = 0;

        }
        int sum = 0;
        for (int t = 0; t < paragraphCount; t++) {
            for (int k = 0; k < sentenceInParagraph[t]; k++) {

                if (t == 0 || (t == paragraphCount - 1)) {
                    LC[sum] = 1.5;
                }
                if (t != 0 && k == 0) {
                    LC[sum] = 1.3;
                }
                if (t != 0 && (t != paragraphCount - 1) && k != 0) {
                    LC[sum] = 1;
                }

                sum++;
            }
            double numerator = 0, denominator = 0;
            numerator = LC[t] * CC[t] * sumtkj[t];
            denominator = modsk[t];
            logicWeight[t] = (numerator / denominator) + (pi[t] / q[t]);
        }

    }

    public void optimalSimilarity() {
        System.out.println("similarity");
        double numerator = 0;
        double denominator = 0;
        double sum1 = 0, sum2 = 0;
        optimalSimilarity = new double[sentenceCount][sentenceCount];

    }

    public void optimalCluster() {
        System.out.println("cluster");

        sentenceIndexLine = new String[clusterCount];

        StringTokenizer st[] = new StringTokenizer[clusterCount];
        StringTokenizer st1;

        sentenceIndexLine[0] = "";
        for (int i = 0; i < sentenceCount; i++) {

            sentenceIndexLine[0] += i + ",";
            if (i < clusterCount && i > 0) {
                sentenceIndexLine[i] = "";
            }
        }

        double max = 0;
        String index;
        int initialClusterCount = 1;
        int clusterElementIndexMin1 = 0, clusterElementIndexMin2 = 0;//two cluster which need to be merged
        int clusterIndexMin1 = 0, clusterIndexMin2 = 0;
        int countp = 0, countq = 0;

        while (initialClusterCount <= clusterCount) {

            System.out.println("Initial cluster count " + initialClusterCount);

            st1 = new StringTokenizer(sentenceIndexLine[0], ",");

            for (int i = 0; i < initialClusterCount; i++) {
                System.out.println("tokenizer loop");
                System.out.println(sentenceIndexLine[i]);
                st[i] = new StringTokenizer(sentenceIndexLine[i], ",");

            }
            System.out.println("Outside tokenizer loop");

            for (int p = 0; p < initialClusterCount; p++) {
                System.out.println("p is" + p);
                countp = 0;
                st[p] = new StringTokenizer(sentenceIndexLine[p], ",");
                while (st[p].hasMoreElements()) {
                    st[p].nextToken();
                    countp++;
                }
                System.out.println("countp " + countp);
                st[p] = new StringTokenizer(sentenceIndexLine[p], ",");

                for (int q = 0; q < initialClusterCount; q++) {
                    System.out.println("q is" + q);
                    countq = 0;
                    st[q] = new StringTokenizer(sentenceIndexLine[q], ",");

                    while (st[q].hasMoreElements()) {
                        st[q].nextToken();
                        countq++;
                    }
                    st[q] = new StringTokenizer(sentenceIndexLine[q], ",");
                    System.out.println("countq " + countq);

                    if (p == 0 && q == 0) {
                        for (int i = 0; i < countp; i++) {

                            int token1 = Integer.parseInt(st[p].nextToken());
                            System.out.println("token1 " + token1);
                            st1 = new StringTokenizer(sentenceIndexLine[0], ",");

                            for (int j = 0; j < countq; j++) {

                                int token2 = Integer.parseInt(st1.nextToken());
                                System.out.println("token2 " + token2);

                                if (token1 != token2) {
                                    double sim = similarity[token1][token2];
                                    if (sim > max) {
                                        max = sim;
                                        clusterElementIndexMin1 = token1;
                                        clusterElementIndexMin2 = token2;
                                        clusterIndexMin1 = p;
                                        clusterIndexMin2 = q;
                                    }
                                }
                            }
                        }

                    }

                    if (p != q && p < q) {
                        st[p] = new StringTokenizer(sentenceIndexLine[p], ",");
                        for (int i = 0; i < countp; i++) {

                            int token1 = Integer.parseInt(st[p].nextToken());
                            System.out.println("token1 " + token1);
                            st[q] = new StringTokenizer(sentenceIndexLine[q], ",");
                            for (int j = 0; j < countq; j++) {

                                int token2 = Integer.parseInt(st[q].nextToken());
                                System.out.println("token2 " + token2);
                                if (token1 != token2) {
                                    double sim = similarity[token1][token2];
                                    if (sim > max) {
                                        max = sim;
                                        clusterElementIndexMin1 = token1;
                                        clusterElementIndexMin2 = token2;
                                        clusterIndexMin1 = p;
                                        clusterIndexMin2 = q;
                                    }
                                }
                            }
                        }

                    }

                }
            }
            System.out.println(max + "    " + clusterElementIndexMin1 + " " + clusterElementIndexMin2 + " " + clusterIndexMin1 + "    " + clusterIndexMin2);
            max = 0;
            if (clusterIndexMin1 == clusterIndexMin2 && clusterIndexMin1 == 0) {
                System.out.println("if 1");
                sentenceIndexLine[clusterIndexMin1] = sentenceIndexLine[clusterIndexMin1].replace(clusterElementIndexMin1 + ",", "");
                sentenceIndexLine[clusterIndexMin2] = sentenceIndexLine[clusterIndexMin2].replace(clusterElementIndexMin2 + ",", "");
                initialClusterCount++;
                System.out.println(initialClusterCount);
                sentenceIndexLine[initialClusterCount - 1] = sentenceIndexLine[initialClusterCount - 1].concat(clusterElementIndexMin1 + ",");
                sentenceIndexLine[initialClusterCount - 1] = sentenceIndexLine[initialClusterCount - 1].concat(clusterElementIndexMin2 + ",");
                System.out.println(sentenceIndexLine[clusterIndexMin1]);
                System.out.println(sentenceIndexLine[initialClusterCount - 1]);

            }
            if (clusterIndexMin1 != clusterIndexMin2 && clusterIndexMin1 == 0) {
                System.out.println("if 2");
                sentenceIndexLine[clusterIndexMin1] = sentenceIndexLine[clusterIndexMin1].replace(clusterElementIndexMin1 + ",", "");
                sentenceIndexLine[clusterIndexMin2] = sentenceIndexLine[clusterIndexMin2].concat(clusterElementIndexMin1 + ",");
                System.out.println(sentenceIndexLine[clusterIndexMin1]);
                System.out.println(sentenceIndexLine[initialClusterCount]);
            }
            if (clusterIndexMin1 != 0 && clusterIndexMin2 != 0) {
                System.out.println("if 3");
                sentenceIndexLine[clusterIndexMin1] = sentenceIndexLine[clusterIndexMin1].concat(sentenceIndexLine[clusterIndexMin2]);
                sentenceIndexLine[clusterIndexMin2] = "";
                initialClusterCount--;
                System.out.println(initialClusterCount);
                System.out.println(sentenceIndexLine[clusterIndexMin1]);
                System.out.println(sentenceIndexLine[clusterIndexMin2]);
            }

            System.out.println("initialClusterCount is" + initialClusterCount);
            System.out.println("");

        }

    }
     */
