package com.example.ipv4subnetcal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class NettDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nett_details);
        Intent i = getIntent();
        int subMask=Integer.parseInt(i.getStringExtra("subMask"));
        String []dividedAddress=i.getStringArrayExtra("dA");
        //Calculate host address range
        int c=-1;
        //loop for indicating at what part the host ranges are located
        System.out.println("subMask "+subMask);
        while(subMask>0){
            subMask-=8;
            c++;
        }
        System.out.println("c "+c);
       int hostAddressfirstPart=Integer.parseInt(dividedAddress[c]);
        subMask=Integer.parseInt(String.valueOf(i.getStringExtra("subMask")));
        //to know how much I should increment in the host part, I subtract number of total bits with number of bits taken from network part
        int adder=(int)Math.pow(2,8-subMask%8);
        if(hostAddressfirstPart>=adder) {
            for (int j = 1; j < hostAddressfirstPart; j += adder) {
                dividedAddress[c] = String.valueOf(j);
            }
        }
        //get the first part of the host range
        String hostRange=dividedAddress[0]+"."+dividedAddress[1]+"."+dividedAddress[2]+"."+dividedAddress[3]+" - ";
        //get the second part of the host range
       /* for(int k=0; k<4; k++){
            if(k==c && c<3){
                int hostAddressSecondPart=Integer.parseInt(dividedAddress[c]);
                hostAddressSecondPart+=adder;
                hostAddressSecondPart-=1;
                dividedAddress[c]=String.valueOf(hostAddressSecondPart);
                hostRange.concat(dividedAddress[c]+".");
            }else if(k==3){//when we get to last part
                hostRange.concat("254");
            }
            hostRange.concat(dividedAddress[k]+".");
        }*/
    System.out.println(hostRange);
    }
}