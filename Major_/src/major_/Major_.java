/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package major_;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Arrays;

/**
 *
 * @author priyankarora
 */
public class Major_ {

    List<String> sentenceDocArray = new ArrayList<String>();//stores sentences in a document
    List<String> allTerms = new ArrayList<String>(); //stores all words
    int sentenceCount = 0;
    int wordCount = 0;
    double tf[][];
    double isf[];
    double weight[][];
    double similarity[][];
    double similarityMatrix[][];
    String sentenceIndexLine[];
    double logicWeight[];
    int clusterCount = 6;
    int paragraphCount = 0;
    int sentenceInParagraph[];
    String impWords[] = {"this paper states", "this paper presents", "this paper discusses", "in a word", "to sum up"}; // for CC
    //title words
    String headingWords[] = {};
    double optimalSimilarity[][];
    double optimalSimilarityMatrix[][];
    String title = "Efficient Clustering of High-Dimensional Data Sets with Application to Reference Matching";
    double titletf[];

    double weighttitle[];
    double simTitleSentence[];
    int representativeSentence[][];

    /*
    
    PREPROCESSING
    
     */
    public void parseFiles(String filePath) throws FileNotFoundException, IOException {
        System.out.println("File Parsing");
        File f = new File(filePath);
        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();
        if (f.getName().endsWith(".txt")) {
            in = new BufferedReader(new FileReader(f));

            String s = null;
            while ((s = in.readLine()) != null) {
                sb.append(s);
            }

        }
        String[] tokenizedTerms = sb.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");   //to get individual terms
        for (String term : tokenizedTerms) {
            if (!allTerms.contains(term)) {  //avoid duplicate entry
                allTerms.add(term);
                //System.out.println(allTerms.get(wordCount));
                wordCount++;
            }
        }
        //System.out.println("wordcount "+ wordCount);

        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
        String s = sb.toString();
        iterator.setText(s);
        int start = iterator.first();
        for (int end = iterator.next();
                end != BreakIterator.DONE;
                start = end, end = iterator.next()) {
            sentenceDocArray.add(s.substring(start, end));

            sentenceCount++;
        }

        Scanner scanner = new Scanner(new FileReader(filePath));
        String str;
        while (scanner.hasNextLine()) {
            // No need to convert to char array before printing

            str = scanner.nextLine();
            paragraphCount++;

        }
        paragraphCount = paragraphCount / 2;

        sentenceInParagraph = new int[paragraphCount];

        scanner = new Scanner(new FileReader(filePath));
        int t = 0;
        while (scanner.hasNextLine()) {
            // No need to convert to char array before printing

            str = scanner.nextLine();

            int index = str.indexOf(".");

            int count = 0;
            while (index != -1) {
                count++;
                str = str.substring(index + 1);
                index = str.indexOf(".");
            }

            if (t % 2 == 0) {
                sentenceInParagraph[t / 2] = count;
            }
            t++;

        }

    }

    public void tF() {
        System.out.println("Tf");
        tf = new double[wordCount][sentenceCount];

        for (int i = 0; i < wordCount; i++) {
            for (int j = 0; j < sentenceCount; j++) {
                StringTokenizer st = new StringTokenizer(sentenceDocArray.get(j), " ");
                while (st.hasMoreTokens()) {

                    if (allTerms.get(i).equals(st.nextToken())) {
                        tf[i][j]++;
                    }

                }
                //System.out.print("  "+allTerms.get(i) +" "+tf[i][j]);
            }
            //System.out.println(" ");

        }
        titletf = new double[wordCount];
        for (int i = 0; i < wordCount; i++) {

            StringTokenizer st = new StringTokenizer(title, " ");
            while (st.hasMoreTokens()) {

                if (allTerms.get(i).equals(st.nextToken())) {
                    titletf[i]++;
                }

            }
            //System.out.print("  "+allTerms.get(i) +" "+tf[i][j]);

            //System.out.println(" ");
        }
    }

    public void iSF() {
        System.out.println("isf");
        isf = new double[wordCount];
        double count = 0;
        for (int i = 0; i < wordCount; i++) {
            for (int j = 0; j < sentenceCount; j++) {
                StringTokenizer st = new StringTokenizer(sentenceDocArray.get(j), " ");
                while (st.hasMoreTokens()) {

                    if (allTerms.get(i).equals(st.nextToken())) {
                        count++;
                        break;
                    }

                }
            }
            //System.out.print(i + "    " + sentenceCount + "  " + count);
            double t = sentenceCount / count;

            isf[i] = Math.log(t);
            //System.out.println("    " + isf[i]);
            count = 1;
        }

    }

    public void tFiSF() {
        System.out.println("tfisf");
        weight = new double[wordCount][sentenceCount];
        for (int i = 0; i < wordCount; i++) {
            for (int j = 0; j < sentenceCount; j++) {
                weight[i][j] = tf[i][j] * isf[i];
                //System.out.println(weight[i][j]);
            }
            //System.out.println("        ");
        }
        weighttitle = new double[wordCount];
        for (int t = 0; t < wordCount; t++) {
            weighttitle[t] = titletf[t] * isf[t];
        }
    }

    public void similarity() {
        System.out.println("similarity");
        double numerator = 0;
        double denominator = 0;
        double sum1 = 0, sum2 = 0;
        similarity = new double[sentenceCount][sentenceCount];
        similarityMatrix = new double[sentenceCount][sentenceCount];
        simTitleSentence = new double[sentenceCount];
        for (int i = 0; i < sentenceCount; i++) {
            for (int j = 0; j < sentenceCount; j++) {
                for (int k = 0; k < wordCount; k++) {
                    numerator = numerator + weight[k][i] * weight[k][j];
                    sum1 = sum1 + Math.pow(weight[k][i], 2);
                    sum2 = sum2 + Math.pow(weight[k][j], 2);
                }
                denominator = Math.sqrt(sum1 * sum2);
                similarity[i][j] = numerator / denominator;
                similarityMatrix[i][j] = similarity[i][j];
                numerator = 0;
                denominator = 0;
                sum1 = 0;
                sum2 = 0;
                //System.out.println("similarity of "+i +" "+j +"is"+ similarity[i][j]);
            }
        }
        simTitleSentence = new double[sentenceCount];
        for (int j = 0; j < sentenceCount; j++) {
            for (int k = 0; k < wordCount; k++) {
                numerator = numerator + weight[k][j] * weighttitle[k];
                sum1 = sum1 + Math.pow(weight[k][j], 2);
                sum2 = sum2 + Math.pow(weighttitle[k], 2);
            }
            denominator = Math.sqrt(sum1 * sum2);
            simTitleSentence[j] = numerator / denominator;

            numerator = 0;
            denominator = 0;
            sum1 = 0;
            sum2 = 0;
            System.out.println("similarity of title with  " + j + "is" + simTitleSentence[j]);
        }

    }

    public void tfIsfCluster() {
        System.out.println("tfisf cluster");

        sentenceIndexLine = new String[clusterCount];

        StringTokenizer st[] = new StringTokenizer[clusterCount];
        StringTokenizer st1;

        sentenceIndexLine[0] = "";
        for (int i = 0; i < sentenceCount; i++) {

            sentenceIndexLine[0] += i + ",,";
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

        while (sentenceIndexLine[0].length() != 0) {

            System.out.println("Initial cluster count " + initialClusterCount);

            st1 = new StringTokenizer(sentenceIndexLine[0], ",,");

            for (int i = 0; i < initialClusterCount; i++) {
                System.out.println("tokenizer loop");
                System.out.println(sentenceIndexLine[i]);
                st[i] = new StringTokenizer(sentenceIndexLine[i], ",,");

            }
            System.out.println("Outside tokenizer loop");

            for (int p = 0; p < initialClusterCount; p++) {
                //System.out.println("p is" + p);
                countp = 0;
                st[p] = new StringTokenizer(sentenceIndexLine[p], ",,");
                while (st[p].hasMoreElements()) {
                    st[p].nextToken();
                    countp++;
                }
                //System.out.println("countp " + countp);
                st[p] = new StringTokenizer(sentenceIndexLine[p], ",");

                for (int q = 0; q < initialClusterCount; q++) {
                    //System.out.println("q is" + q);
                    countq = 0;
                    st[q] = new StringTokenizer(sentenceIndexLine[q], ",,");

                    while (st[q].hasMoreElements()) {
                        st[q].nextToken();
                        countq++;
                    }
                    st[q] = new StringTokenizer(sentenceIndexLine[q], ",,");
                    //System.out.println("countq " + countq);

                    if (p == 0 && q == 0) {
                        for (int i = 0; i < countp; i++) {

                            int token1 = Integer.parseInt(st[p].nextToken());

                            st1 = new StringTokenizer(sentenceIndexLine[0], ",,");

                            for (int j = 0; j < countq; j++) {

                                int token2 = Integer.parseInt(st1.nextToken());

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
                        st[p] = new StringTokenizer(sentenceIndexLine[p], ",,");
                        for (int i = 0; i < countp; i++) {

                            int token1 = Integer.parseInt(st[p].nextToken());

                            st[q] = new StringTokenizer(sentenceIndexLine[q], ",,");
                            for (int j = 0; j < countq; j++) {

                                int token2 = Integer.parseInt(st[q].nextToken());

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
            System.out.println(max + "    " + clusterElementIndexMin1 + "   " + clusterElementIndexMin2 + "   " + clusterIndexMin1 + "    " + clusterIndexMin2);
            max = 0;
            if (clusterIndexMin1 == clusterIndexMin2 && clusterIndexMin1 == 0) {
                System.out.println("if 1");
                System.out.println("found element is");
                if (clusterElementIndexMin1 != 0) {
                    sentenceIndexLine[clusterIndexMin2] = sentenceIndexLine[clusterIndexMin2].replaceFirst("," + clusterElementIndexMin2 + ",", "");
                    sentenceIndexLine[clusterIndexMin1] = sentenceIndexLine[clusterIndexMin1].replaceFirst("," + clusterElementIndexMin1 + ",", "");
                    initialClusterCount++;
                    System.out.println(initialClusterCount);
                    sentenceIndexLine[initialClusterCount - 1] = sentenceIndexLine[initialClusterCount - 1].concat(clusterElementIndexMin1 + ",,");
                    sentenceIndexLine[initialClusterCount - 1] = sentenceIndexLine[initialClusterCount - 1].concat(clusterElementIndexMin2 + ",,");
                    System.out.println(sentenceIndexLine[clusterIndexMin1]);
                    System.out.println(sentenceIndexLine[initialClusterCount - 1]);
                }
                if (clusterElementIndexMin1 == 0) {
                    sentenceIndexLine[clusterIndexMin2] = sentenceIndexLine[clusterIndexMin2].replaceFirst("," + clusterElementIndexMin2 + ",", "");
                    sentenceIndexLine[clusterIndexMin1] = sentenceIndexLine[clusterIndexMin1].replaceFirst(clusterElementIndexMin1 + ",,", "");
                    initialClusterCount++;
                    System.out.println(initialClusterCount);
                    sentenceIndexLine[initialClusterCount - 1] = sentenceIndexLine[initialClusterCount - 1].concat(clusterElementIndexMin1 + ",,");
                    sentenceIndexLine[initialClusterCount - 1] = sentenceIndexLine[initialClusterCount - 1].concat(clusterElementIndexMin2 + ",,");
                    System.out.println(sentenceIndexLine[clusterIndexMin1]);
                    System.out.println(sentenceIndexLine[initialClusterCount - 1]);
                }
            }
            if (clusterIndexMin1 != clusterIndexMin2 && clusterIndexMin1 == 0) {
                System.out.println("if 2");
                if (clusterElementIndexMin1 != 0) {
                    sentenceIndexLine[clusterIndexMin1] = sentenceIndexLine[clusterIndexMin1].replaceFirst("," + clusterElementIndexMin1 + ",", "");
                    sentenceIndexLine[clusterIndexMin2] = sentenceIndexLine[clusterIndexMin2].concat(clusterElementIndexMin1 + ",,");
                    System.out.println(sentenceIndexLine[clusterIndexMin1]);
                    System.out.println(sentenceIndexLine[initialClusterCount]);
                }
                if (clusterElementIndexMin1 == 0) {
                    sentenceIndexLine[clusterIndexMin1] = sentenceIndexLine[clusterIndexMin1].replaceFirst(clusterElementIndexMin1 + ",,", "");
                    sentenceIndexLine[clusterIndexMin2] = sentenceIndexLine[clusterIndexMin2].concat(clusterElementIndexMin1 + ",,");
                    System.out.println(sentenceIndexLine[clusterIndexMin1]);
                    System.out.println(sentenceIndexLine[initialClusterCount]);
                }
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
            System.out.println("-------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------");

        }

    }

    public void effectiveCluster() {
        StringTokenizer st = new StringTokenizer(sentenceIndexLine[0], ",,");
        int countofcluster = clusterCount - 1;
        while (st.hasMoreTokens()) {
            int t = Integer.parseInt(st.nextToken());
            double max=0;
            int indexcluster=0;
            int element=0;
            for (int k = 1; k <= countofcluster; k++) {

                
                StringTokenizer st1 = new StringTokenizer(sentenceIndexLine[k], ",,");
                while (st1.hasMoreTokens()) {
                    
                    int ind=Integer.parseInt(st1.nextToken());
                    if(similarity[t][ind]>max){max=similarity[t][ind];indexcluster=k;element=ind;}
                }

                
            }
            sentenceIndexLine[0]=sentenceIndexLine[0].replaceFirst(t+",,", "");
            sentenceIndexLine[indexcluster]=sentenceIndexLine[indexcluster].concat(t+",,");
        }
    }

    public void clusterElements() {
        System.out.println("----------------STOP----------------");

        System.out.println("clusterElements");
        for (int i = 0; i < clusterCount; i++) {
            System.out.println("C "+i+": "+sentenceIndexLine[i]);
        }
    }

    public void ranking(){}
    
    public void representativeSentences() {
        System.out.println("Representative Sentences");
        //select from each cluster, 2 sentences holding highest similarity with title
        representativeSentence = new int[clusterCount][2];
        for (int t = 0; t < clusterCount; t++) {
            StringTokenizer st = new StringTokenizer(sentenceIndexLine[t], ",,");
            int count1 = 0;
            int count2 = 0;
            double max1 = 0;
            double max2 = 0;
            while (st.hasMoreTokens()) {

                String term = st.nextToken();
                //System.out.println(term);
                int a = Integer.parseInt(term);
                if (simTitleSentence[a] >= max1) {
                    max1 = simTitleSentence[a];
                    count1 = a;
                }
            }

            representativeSentence[t][0] = count1;
            //System.out.println(count1);
            sentenceIndexLine[t] = sentenceIndexLine[t].replaceFirst(count1 + ",,", "");
            StringTokenizer st1 = new StringTokenizer(sentenceIndexLine[t], ",,");
            while (st1.hasMoreTokens()) {

                String term = st1.nextToken();
                //System.out.println(term);
                int a = Integer.parseInt(term);
                if (simTitleSentence[a] >= max2) {
                    max2 = simTitleSentence[a];
                    count2 = a;
                }
            }

            representativeSentence[t][1] = count2;
            //System.out.println(count2);
        }

    }

    public void display() {
        System.out.println("Display");
        for (int t = 0; t < clusterCount; t++) {
            for (int k = 0; k < 2; k++) {
                System.out.println(sentenceDocArray.get(representativeSentence[t][k]));
            }
        }
    }

    public void representativeSentences1() {
        System.out.println("representative Sentences 11");
        double simtitle[] = new double[sentenceCount];
        for (int t = 0; t < sentenceCount; t++) {
            simtitle[t] = simTitleSentence[t];
        }
        Arrays.sort(simtitle);
        System.out.println("After Arrays.sort");
        for (int t = 0; t < sentenceCount; t++) {
            System.out.println(simtitle[t]);
        }
        for (int t = 0; t < sentenceCount / 2; t++) {
            double p = simtitle[t];
            simtitle[t] = simtitle[sentenceCount - t - 1];
            simtitle[sentenceCount - t - 1] = p;
        }
        System.out.println("After Reverse");
        for (int t = 0; t < sentenceCount; t++) {
            System.out.println(simtitle[t]);
        }
        for (int t = 0; t < clusterCount * 2; t++) {
            for (int k = 0; k < sentenceCount; k++) {
                if (simtitle[t] == simTitleSentence[k]) {
                    System.out.println("Sentence " +k+": "+ sentenceDocArray.get(k));
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, FileNotFoundException {
        // TODO code application logic here
        Major_ mj = new Major_();
        mj.parseFiles("/Users/priyankarora/Documents/abc.txt");
        mj.tF();
        mj.iSF();
        mj.tFiSF();
        mj.similarity();
        try {
            mj.tfIsfCluster();
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        mj.effectiveCluster();
        mj.clusterElements();
        mj.representativeSentences();
        mj.representativeSentences1();
        //mj.display();
    }

}
