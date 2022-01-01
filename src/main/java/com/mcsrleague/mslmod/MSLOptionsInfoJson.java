package com.mcsrleague.mslmod;

class MSLOptionsInfoJson {
    public Integer difficulty = null;
    public Boolean timerEnabled = null;
    public Boolean timerShadow = null;
    public Double timerX = null;
    public Double timerY = null;

    public void ensure(){
        if(difficulty == null){
            difficulty = 1;
        }
        if(timerEnabled == null){
            timerEnabled = true;
        }
        if(timerShadow == null){
            timerShadow = true;
        }
        if(timerX == null){
            timerX = 0.9921875d;
        }
        if(timerY == null){
            timerY = 0.02064896755162242d;
        }
    }
}
