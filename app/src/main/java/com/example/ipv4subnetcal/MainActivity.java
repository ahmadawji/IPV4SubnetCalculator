package com.example.ipv4subnetcal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    EditText networkAddress;
    Spinner netPart,numSub;
    TextView numHostSub, subnetMask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        networkAddress=findViewById(R.id.etNetworkAddress);
        netPart=findViewById(R.id.spNetRanges);
        numSub= findViewById(R.id.spNumSub);
        numHostSub= findViewById(R.id.tvNumHostSub);
        subnetMask=findViewById(R.id.tvSubMask);
    }

    public void calSubnets(View view) {
        String address;
        address= networkAddress.getText().toString();
        int nP = Integer.parseInt(netPart.getSelectedItem().toString());
        if(checkIP(address)){
            Toast.makeText(this, "np: "+nP, Toast.LENGTH_SHORT).show();
            int subnets= 32-nP;
            ArrayList<Integer>arraylist= new ArrayList<>();

            for(int i=0; i<=subnets; i++){
                arraylist.add((int)Math.pow(2,i));
            }

            ArrayAdapter<Integer> arrayAdapter= new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item,arraylist);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            numSub.setAdapter(arrayAdapter);
        }

    }
    public boolean checkIP(String address){
        // Regex for digit from 0 to 255.
        String zeroTo255 = "(\\d{1,2}|(0|1)\\d{2}|2[0-4]\\d|25[0-5])";
        // Regex for a digit from 0 to 255 and
        // followed by a dot, repeat 4 times.
        // this is the regex to validate an IP address.
        String regex = zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;

        Pattern p = Pattern.compile(regex);
        if(address.isEmpty()){
            Toast.makeText(this, "Please Enter an ip address", Toast.LENGTH_LONG).show();
            return false;
        }else{
            Matcher m = p.matcher(address);
            if(m.matches()){
                Toast.makeText(this, "right ip", Toast.LENGTH_LONG).show();
                return true;
            }else{
                Toast.makeText(this, "Please enter valid ip address", Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }

    public void otherNetDetails(View view) {
        String nS=numSub.getSelectedItem().toString();
        if(nS.isEmpty()){
            Toast.makeText(this, "Calculate first number of available subnets", Toast.LENGTH_LONG).show();
        }else{
            int numberSubnets=Integer.parseInt(nS);
            int np, hosts;
            np=Integer.parseInt(netPart.getSelectedItem().toString());

            int c=0;
            while(numberSubnets>1){
                numberSubnets/=2;
                c++;
            }
            /*hosts will calculate number of hosts per subnet
            * To calculate number of subnets you subtract the total number of bits with bits reserved for the network part + the bits you borrowed from the host part
            *
            * after that you can determine number of host where numHosts = 2^(remainder bits)
            *
            * */
            hosts=(int)Math.pow(2,32 - np - c);
            //Toast.makeText(this,"hosts: "+ hosts, Toast.LENGTH_LONG).show();

            /*Calculate Subnet Mask
            * To calculate subnet mask of each network:
            * First scenario we check the network part if its 8 then all the network bits are 1s '11111111'
            * Second if its >=8, for example 12, we keep on subtracting until its zero, 12>=8=> first 8 bits are full, 12-8=4, then this indicates that only the first 4 bits of the second ip part is full and the rest are zeros (Rule: 2^(numFullBits)-2^(numFullBits - restBits))
            * 11111111-00111111=11000000(needed result)
            * */
            int [] mask= new int[4];
            int remainder= np+c;
            Toast.makeText(this,"remainder: "+ remainder, Toast.LENGTH_LONG).show();
            for(int i=0; i<mask.length; i++){
                if(remainder>=8){
                    mask[i]=(int)Math.pow(2,8)-1;
                    remainder-=8;
                }else if(remainder<8 && remainder>0){
                    mask[i]=((int)Math.pow(2,8)-(int)Math.pow(2,8-remainder));
                    remainder-=8;
                }else{
                    mask[i]=0;
                }
            }
            remainder=np+c;
            numHostSub.setText(String.valueOf(hosts));
            subnetMask.setText(mask[0]+"."+mask[1]+"."+mask[2]+"."+mask[3]+"/"+remainder);
        }
    }

    public void moreNetDetails(View view) {
        try {
        String address= networkAddress.getText().toString();
        //get the last chart of subnet mask for identifying final network part to calculate host address range
        String subMask=subnetMask.getText().toString().substring(subnetMask.getText().toString().indexOf('/')+1);
        String []dividedAddress=address.split("\\.");
        if (checkIP(address)) {
            Intent i = new Intent(this, NettDetails.class);
            i.putExtra("dA", dividedAddress);
            System.out.println("subMask "+subMask);
            i.putExtra("subMask", subMask);
            startActivity(i);
        }
        }catch (Exception ex){
            Toast.makeText(this, "Please make sure the ip address or the number of subnet/s are not empty", Toast.LENGTH_LONG).show();
        }

    }
}