package com.dilemma.chickenortheegg;

public  class MyMessage {
    public Position position; // левый/правый
    public Event event; // какое событие нужно отрисовывать
    public MyMessage(Position position, Event event){
        this.position = position;
        this.event = event;
    }
}
