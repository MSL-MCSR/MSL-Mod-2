package com.mcsrleague.mslmod;

class MSLOptionsInfoJson {
    public Integer difficulty = null;
    public Boolean timerEnabled = null;
    public Integer timerX = null;
    public Integer timerY = null;

    public void ensure(){
        if(difficulty == null){
            difficulty = 1;
        }
        if(timerEnabled == null){
            timerEnabled = false;
        }
        if(timerX == null){
            timerX = 10;
        }
        if(timerY == null){
            timerY = 10;
        }
    }
}
