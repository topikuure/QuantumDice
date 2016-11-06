package topikuure.quantumdice;

/**
 * Created by Topi on 06/11/2016.
 *
 * QuantumRandom käyttää QRNG@ANU JSON API -rajapintaa
 * https://qrng.anu.edu.au/API/api-demo.php
 */
public class QuantumRandom {

    private int bufferTopIndex = 0;
    private int buffer[] = {48,223,28,238,228,72,151,179,168,2};
    private String arrayLength = "10";

    public int getRandomNumber(int min, int max) {
        if(bufferTopIndex >= buffer.length) fillBuffer();
        return (buffer[bufferTopIndex++] % (max - min + 1)) + min;
    }

    private boolean fillBuffer() {//TODO implementoi
        //String url = "https://qrng.anu.edu.au/API/jsonI.php?length=" + arrayLength + "&type=uint8";
        //Hae bufferiin data netistä
        bufferTopIndex = 0;
        return false;
    }
}