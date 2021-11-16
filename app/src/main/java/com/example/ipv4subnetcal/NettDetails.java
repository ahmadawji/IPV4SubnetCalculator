package com.example.ipv4subnetcal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class NettDetails extends AppCompatActivity {
TextView netClass,hostRange,broadAddress,wildMask, cNotation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nett_details);
        netClass=findViewById(R.id.tvNetClass);
        hostRange=findViewById(R.id.tvHostRange);
        broadAddress=findViewById(R.id.tvBroadAddress);
        wildMask=findViewById(R.id.tvWildMask);
        cNotation=findViewById(R.id.tvCNotation);

        Intent i = getIntent();
        int subMask=Integer.parseInt(i.getStringExtra("subMask"));
        String []dividedAddress=i.getStringArrayExtra("dA");

        //Calculate net class
        int classBits= Integer.parseInt(dividedAddress[0]);
        if (classBits>=0 && classBits<128){
            netClass.setText("Class A");
        }else if(classBits>=128 && classBits<192){
            netClass.setText("Class B");
        }else if(classBits>=192 && classBits<240){
            netClass.setText("Class C");
        }else{
            netClass.setText("Class D");
        }


        //Calculate host address range
        int c=0;
        //if else block for indicating at what part the host ranges are located
        if(subMask>8 && subMask<16) c=1;
        else if(subMask>16 && subMask<24) c=2;
        else c=3;
        System.out.println("c "+c);

       int hostAddressFirstPart=Integer.parseInt(dividedAddress[c]);
        subMask=Integer.parseInt(String.valueOf(i.getStringExtra("subMask")));
        //to know how much I should increment in the host part, I subtract number of total bits with number of bits taken from network part
        int adder=(int)Math.pow(2,8-subMask%8);
        if(hostAddressFirstPart>=adder) {
            int j;
            if(c<3){
                j=0;
            }else{//if the varying octet was the last one we have to add one that identify the first host
                j=1;
            }

            while(j+adder <= hostAddressFirstPart){
                j += adder;
            }
            dividedAddress[c] = String.valueOf(j);
        }

        //Calculate CIDR notation
        String cN="";
        for(int j=0; j<4; j++){
            if(j==3){
                if (j>c)cN+="0";
                else cN+=dividedAddress[j];
            }else{
                if (j>c)cN+="0"+".";
                else cN+=dividedAddress[j]+".";
            }
        }
        cNotation.setText(cN+"/"+subMask);

        //get the first part of the host range
        //This loop is needed to set the host bits after the varying part(the combination of net bits and host bits). => all octets after the varying octet is equal to 0 except for the last octet which refers to first host in the network and its equal to 1
        for(int j=c+1; j<4; j++){
            if (j==3)dividedAddress[j]="1";
            else dividedAddress[j]="0";
        }


        String hostAddressRange=dividedAddress[0]+"."+dividedAddress[1]+"."+dividedAddress[2]+"."+dividedAddress[3]+" - ";
        //get the second part of the host range
       for(int k=0; k<4; k++) {
               if(k<c){
                   hostAddressRange+=dividedAddress[k] + ".";
               }
               else if (k == c && k<=3) {//If we are in the ip part where the host address range vary

                   int hostAddressSecondPart = Integer.parseInt(dividedAddress[k]);
                   hostAddressSecondPart += adder;
                   if(k==3){
                   hostAddressSecondPart -= 3;
                   dividedAddress[k] = String.valueOf(hostAddressSecondPart);
                   hostAddressRange+=dividedAddress[k] ;
                   }
                   else{
                   hostAddressSecondPart -= 1;
                   dividedAddress[k] = String.valueOf(hostAddressSecondPart);
                   hostAddressRange+=dividedAddress[k] + ".";
                   }
               } else if (k > c && k<3) {//when we get to last part
                   hostAddressRange+="255.";
               }else{
                   hostAddressRange+="254";
               }
       }
    System.out.println(hostAddressRange);
       hostRange.setText(hostAddressRange);

       //set broadcast address
        String broadRange=hostAddressRange.substring(hostAddressRange.indexOf("-")+1).trim();
        String[]bA=broadRange.split("\\.");
        bA[3]= String.valueOf(Integer.parseInt(bA[3])+1);
        broadAddress.setText(bA[0]+"."+bA[1]+"."+bA[2]+"."+bA[3]);

       //Calculate wildcard mask
         String wM="";
         System.out.println("adder"+adder);
         for(int j=0 ; j<4; j++){
             if(j==c){
                 if(j<3)
                 wM+=String.valueOf(adder-1)+".";
                 else
                 wM+=String.valueOf(adder-1);
             }else if(j>c){
                 if(j<3)
                    wM+="255.";
                 else
                    wM+="255";
             }else{
                 wM+="0.";
             }
         }
         wildMask.setText(wM);

    }
}