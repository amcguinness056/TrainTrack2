public class TrainAProcess extends Thread {
    // Note This process is used to emulate a train as it proceeds around the track

    String trainName;
    TrainTrack theTrack;
    //initialise (constructor)
    public TrainAProcess(String trainName, TrainTrack theTrack) {
        this.trainName = trainName;
        this.theTrack = theTrack;
    }

    @Override
    public void run() {   // start train Process
        theTrack.trainA_MoveOnToTrack(trainName); // move on to track A
        theTrack.trainAMoveAroundTrack(trainName); // move around A loop
        theTrack.trainA_MoveOffTrack(trainName); // move off the track */
    } // end run


}
