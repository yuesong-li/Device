int pb0=8;
int pb1=9;
int pb2=10;
int pb3=11;
int pb4=12;
int pb5=13;
int pc0=0;
int pc1=A1;
int pc2=A2;
int pc3=3;
int pc4=4;
int pc5=5;
int pd0=0;
int pd1=1;
int pd2=2;
int pd3=3;
int pd4=4;
int pd5=5;
int pd6=6;
int pd7=7;
int ib;
int RobAlarm;
int FireAlarm;
int Waterleackeg;
int Spis;
int Window;
int Powercut;
int lofttemp;
int roomtemp;
long starttime;
long endtime;
long temp;
boolean start = false;
boolean alarmSent = false;
void setup() {
  pinMode(pb0, OUTPUT);
  pinMode(pb1, OUTPUT);
  pinMode(pb2,OUTPUT);
  pinMode(pb3, OUTPUT);
  pinMode(pb4, OUTPUT);
  pinMode(pb5,OUTPUT);
  pinMode(pd2,INPUT);
  pinMode(pd3,OUTPUT);
  delay(100);
  digitalWrite(pb0, LOW);
  digitalWrite(pb3, LOW);
  digitalWrite(pb5, LOW);
  digitalWrite(pb4, LOW);
  delay(100);
  //lightOut off
  digitalWrite(pb0, HIGH);
  digitalWrite(pb3, HIGH);
  digitalWrite(pb5, HIGH);
  digitalWrite(pb4,HIGH);
  //  Serial.println(2);////////inform: lightOut off
  delay(100);
  //lightIn off
  digitalWrite(pb0, LOW);
  digitalWrite(pb3, HIGH);
  digitalWrite(pb5, LOW);
  digitalWrite(pb4,HIGH);
  //  Serial.println(4);////////inform: lightIn off
  delay(100);
  //heaterRoom off
  digitalWrite(pb0, HIGH);
  digitalWrite(pb3, LOW);
  digitalWrite(pb5, HIGH);
  digitalWrite(pb4, HIGH);
  //  Serial.println(8);////////inform: heaterRoom off
  delay(100);
  //heaterLoft off
  digitalWrite(pb0, LOW);
  digitalWrite(pb3, LOW);
  digitalWrite(pb5, HIGH);
  digitalWrite(pb4, HIGH);
  //Serial.println(10);////////inform: heaterLoft off
  delay(100);
  digitalWrite(pb0, LOW);
  digitalWrite(pb3, HIGH);
  digitalWrite(pb5, HIGH);
  digitalWrite(pb4, HIGH);
  delay(100);
  digitalWrite(pb0, HIGH);
  digitalWrite(pb3, LOW);
  digitalWrite(pb5, LOW);
  digitalWrite(pb4, HIGH);
  delay(100);
  digitalWrite(pb0, HIGH);
  digitalWrite(pb3, HIGH);
  digitalWrite(pb5, LOW);
  digitalWrite(pb4, HIGH);
  delay(100);
  //fan off
  digitalWrite(pb2, LOW);
  //Serial.println(18);////////inform: fan off
  delay(100);
  digitalWrite(pd7, LOW);
  Serial.begin(9600);
  start =true;

}

void loop() {
  roomtemp = (5.0 * analogRead(pc2) * 100.0) / 1024;
  lofttemp = (5.0 * analogRead(pc1) * 100.0) / 1024;
  //door closed
  if(digitalRead(pd3)==HIGH){
    digitalWrite(pb0, LOW);
    digitalWrite(pb3, LOW);
    digitalWrite(pb5, LOW);
    digitalWrite(pb4, LOW);
    if(start==false){
      //indicate the alarm is off
      Serial.println("az");
    }
    start = true;
  }     
  //door open
  if(digitalRead(pd3)==LOW){
    if(start == true){
      starttime = millis();
      start = false;
      alarmSent = false;
    }
  }
  if(start==false){
    if((millis() - starttime) > 3000){
      digitalWrite(pb0, LOW);
      digitalWrite(pb3, LOW);
      digitalWrite(pb5, LOW);
      digitalWrite(pb4, HIGH);
      if(alarmSent == false){
        //'ad' stands for alarm:door
        Serial.println("ad");
        alarmSent = true;
      }
      delay(100);
    }
  }

  RobAlarm = digitalRead(pd3);
  FireAlarm = digitalRead(pd2);
  Waterleackeg = digitalRead(pd4);
  Spis= digitalRead(pd5);
  Window = digitalRead(pd6);
  Powercut= digitalRead(pd7);
  //temp alarm
  if(roomtemp > 40 || lofttemp > 40){
    digitalWrite(pb0, LOW);
    digitalWrite(pb3, LOW);
    digitalWrite(pb5, LOW);
    digitalWrite(pb4, HIGH);
    //alarm:fire
    Serial.println("af");
    delay(100);
  }
  ib = Serial.read();
  if(ib==1){
    digitalWrite(pb0, HIGH);
    digitalWrite(pb3, HIGH);
    digitalWrite(pb5, HIGH);
    digitalWrite(pb4,LOW);
    delay(200);
    digitalWrite(pb0, HIGH);
    digitalWrite(pb3, HIGH);
    digitalWrite(pb5, LOW);
    digitalWrite(pb4, HIGH);
    delay(100);
  }
  if(ib==2){
    digitalWrite(pb0, HIGH);
    digitalWrite(pb3, HIGH);
    digitalWrite(pb5, HIGH);
    digitalWrite(pb4,HIGH);
    delay(200);
    digitalWrite(pb0, HIGH);
    digitalWrite(pb3, HIGH);
    digitalWrite(pb5, LOW);
    digitalWrite(pb4, HIGH);
    delay(100);
  }
  if(ib==3){
    digitalWrite(pb0, LOW);
    digitalWrite(pb3, HIGH);
    digitalWrite(pb5, LOW);
    digitalWrite(pb4,LOW);
    delay(100);
  }
  if(ib==4){
    digitalWrite(pb0, LOW);
    digitalWrite(pb3, HIGH);
    digitalWrite(pb5, LOW);
    digitalWrite(pb4,HIGH);
    delay(100);
  }
  if(ib==5){
    digitalWrite(pb0, LOW);
    digitalWrite(pb3, LOW);
    digitalWrite(pb5, LOW);
    digitalWrite(pb4, HIGH);
    delay(100);
  }
  if(ib==6){
    digitalWrite(pb0, LOW);
    digitalWrite(pb3, LOW);
    digitalWrite(pb5, LOW);
    digitalWrite(pb4, LOW);
    delay(100);
  }
  if(ib==7){
    digitalWrite(pb0, HIGH);
    digitalWrite(pb3, LOW);
    digitalWrite(pb5, HIGH);
    digitalWrite(pb4, LOW);
    delay(100);
  }
  if(ib==8){
    digitalWrite(pb0, HIGH);
    digitalWrite(pb3, LOW);
    digitalWrite(pb5, HIGH);
    digitalWrite(pb4, HIGH);
    delay(100);
  }
  if(ib==9){
    digitalWrite(pb0, LOW);
    digitalWrite(pb3, LOW);
    digitalWrite(pb5, HIGH);
    digitalWrite(pb4, LOW);
    delay(100);
  }
  if(ib==10){
    digitalWrite(pb0, LOW);
    digitalWrite(pb3, LOW);
    digitalWrite(pb5, HIGH);
    digitalWrite(pb4, HIGH);
    delay(100);
  }
  if(ib==11){
    digitalWrite(pb0, LOW);
    digitalWrite(pb3, HIGH);
    digitalWrite(pb5, HIGH);
    digitalWrite(pb4, LOW);
    delay(100);
  }
  if(ib==12){
    digitalWrite(pb0, LOW);
    digitalWrite(pb3, HIGH);
    digitalWrite(pb5, HIGH);
    digitalWrite(pb4, HIGH);
    delay(100);
  }
  if(ib==13){
    digitalWrite(pb0, HIGH);
    digitalWrite(pb3, LOW);
    digitalWrite(pb5, LOW);
    digitalWrite(pb4, LOW);
    delay(100);
  }
  if(ib==14){
    digitalWrite(pb0, HIGH);
    digitalWrite(pb3, LOW);
    digitalWrite(pb5, LOW);
    digitalWrite(pb4, HIGH);
    delay(100);
  }
  if(ib==15){
    digitalWrite(pb0, HIGH);
    digitalWrite(pb3, HIGH);
    digitalWrite(pb5, LOW);
    digitalWrite(pb4, LOW);
    delay(100);
  }
  if(ib==16){
    digitalWrite(pb0, HIGH);
    digitalWrite(pb3, HIGH);
    digitalWrite(pb5, LOW);
    digitalWrite(pb4, HIGH);
    delay(100);
  }
  if(ib==17){
    digitalWrite(pb2, HIGH);
    delay(100);
  }
  if(ib==18){
    digitalWrite(pb2, LOW);
    delay(100);
  }
  if(ib==19){
    digitalWrite(pd7, HIGH);
    delay(100);
  }
  if(ib==20){
    digitalWrite(pd7, LOW);
    delay(100);
  }
  if(ib==21){
    roomtemp = (5.0 * analogRead(pc2) * 100.0) / 1024;
    Serial.println(roomtemp);
    delay(100);
  }
  if(ib==22){
    lofttemp = (5.0 * analogRead(pc1) * 100.0) / 1024;
    Serial.println(lofttemp);
    delay(100);
  }
  if(ib==23){
    delay(100);
    digitalWrite(pb0, LOW);
    digitalWrite(pb3, LOW);
    digitalWrite(pb5, LOW);
    digitalWrite(pb4, HIGH);

    for(int i=0;i<30;i++){
      delay(100);
      digitalWrite(pb0, LOW);
      digitalWrite(pb3, LOW);
      digitalWrite(pb5, LOW);
      digitalWrite(pb4, LOW);
    }
    delay(100);
    digitalWrite(pb0, LOW);
    digitalWrite(pb3, LOW);
    digitalWrite(pb5, LOW);
    digitalWrite(pb4, HIGH);
  }
  if(ib==24){
  }

  if(FireAlarm==HIGH){
    delay(100);
    digitalWrite(pb0, LOW);
    digitalWrite(pb3, LOW);
    digitalWrite(pb5, LOW);
    digitalWrite(pb4,HIGH);
  }
  else {
    delay(100);
    digitalWrite(pb0, LOW);
    digitalWrite(pb3, LOW);
    digitalWrite(pb5, LOW);
    digitalWrite(pb4,LOW);
  }
}




