public class TrainBProcess extends Thread {
    // Note This process is used to emulate a train as it proceeds around the track
    String trainName;
    TrainTrack theTrack;
    //initialise (constructor)
    public TrainBProcess(String trainName, TrainTrack theTrack) {
        this.trainName = trainName;
        this.theTrack = theTrack;
    }

    @Override
    public void run() {   // start train Process
        theTrack.trainB_MoveOnToTrack(trainName); // move on to track B
        theTrack.trainBMoveAroundTrack(trainName); // move around B loop
        theTrack.trainB_MoveOffTrack(trainName); // move off the track
    } // end run

}
