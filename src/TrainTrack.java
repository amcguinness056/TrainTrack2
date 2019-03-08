public class TrainTrack {
    private final String[] slots = {"[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]",
            "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]"};

    // declare array to hold the Binary Semaphores for access to track slots (sections)
    private final MageeSemaphore slotSem[] = new MageeSemaphore[22];

    // reference to train activity record
    Activity theTrainActivity;

    // counting semaphore to limit number of trains on track
    MageeSemaphore aCountSem;
    MageeSemaphore bCountSem;

    /* Constructor for TrainTrack */
    public TrainTrack() {
        // record the train activity
        theTrainActivity = new Activity(slots);
        // create the array of slotSems and set them all free (empty)
        for (int i = 0; i < 22; i++) {
            slotSem[i] = new MageeSemaphore(1);
        }
        // create  semaphores for limiting number of trains on track
        aCountSem = new MageeSemaphore(4);
        bCountSem = new MageeSemaphore(4);
    }  // constructor

    public void trainA_MoveOnToTrack(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 100));
        aCountSem.P(); // limit  number of trains on track to avoid deadlock
        // record the train activity
        slotSem[0].P();// wait for slot 0 to be free
        slots[0] = "[" + trainName + "]"; // move train type A on to slot zero
        theTrainActivity.addMovedTo(0); // record the train activity
    }// end trainA_movedOnToTrack

    public void trainB_MoveOnToTrack(String trainName) {
        // record the train activity
        bCountSem.P();  // limit  number of trains on track to avoid deadlock
        CDS.idleQuietly((int) (Math.random() * 100));
        slotSem[12].P();// wait for slot 12 to be free
        slots[12] = "[" + trainName + "]"; // move train type B on to slot 12
        theTrainActivity.addMovedTo(12); // record the train activity
    }// end trainB_movedOnToTrack

    public void trainAMoveAroundTrack(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 100));
        int currentPosition = 0;
        do {
            if(currentPosition == 3){ //check position before junction  to see if slot ahead of junction is also free to avoid stopping on junction
                slotSem[4].P(); //wait until Junction 4 is free
                slotSem[5].P(); //wait until slot ahead of junction is free
                slots[4] = slots[3]; // move across junction 4
                slots[3] = "[..]"; // clear slot 3
                theTrainActivity.addMovingThroughJunction(4); // record moving through junction
                slots[5] = slots[4]; // move into slot 5
                slots[4] = "[..]"; // clear junction 4
                slotSem[4].V(); // signal junction that you are leaving
                slotSem[3].V(); // signal slot 3 that you are leaving
                theTrainActivity.addMovedTo(5); // record train has moved to slot 5
                currentPosition = 5; // update current position to 5

            } else if(currentPosition == 6) {//check position before junction  to see if slot ahead of junction is also free to avoid stopping on junction
                slotSem[7].P(); //wait until junction 7 is free
                slotSem[8].P(); //wait until slot ahead of junction is free
                slots[7] = slots[6];// move into junction 7
                slots[6] = "[..]"; // clear slot 6
                theTrainActivity.addMovingThroughJunction(7); // record moving through junction
                slots[8] = slots[7]; // move into slot 8
                slots[7] = "[..]"; // clear junction 7
                slotSem[7].V(); // signal junction that you have left
                slotSem[6].V(); // signal slot 6 that you have left
                theTrainActivity.addMovedTo(8); //record train has moved to slot 8
                currentPosition = 8;
            } else {
                // wait until the position ahead is empty and then move into it
                slotSem[currentPosition + 1].P(); // wait for the slot ahead to be free
                slots[currentPosition + 1] = slots[currentPosition]; // move train forward one position
                slots[currentPosition] = "[..]"; // clear the slot the train vacated
                theTrainActivity.addMovedTo(currentPosition + 1); // record the train activity
                slotSem[currentPosition].V(); // signal slot you are leaving
                currentPosition++;
            }
        } while (currentPosition < 11);
        CDS.idleQuietly((int) (Math.random() * 100));
    } // end trainAMoveAroundTrack

    public void trainBMoveAroundTrack(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 100));
        int currentPosition = 12;
        do {
            if(currentPosition == 15){ //check position before junction to see if slot ahead of junction is also free to avoid stopping on junction
                slotSem[7].P(); //wait until junction 7 is free
                slotSem[16].P(); //wait until slot ahead of junction, slot 16, is free
                slots[7] = slots[15]; // move into junction
                slots[15] = "[..]"; // clear slot 15
                theTrainActivity.addMovingThroughJunction(7); //record train moving across junction
                slots[16] = slots[7]; // move into slot 16
                slots[7] = "[..]"; // clear slot 7
                slotSem[7].V(); // signal junction 7 that you are leaving
                slotSem[15].V(); // signal slot 15 that you are leaving
                theTrainActivity.addMovedTo(16); //record that train has moved to slot 16
                currentPosition++;
            } else if(currentPosition == 17){//check position before junction to see if slot ahead of junction is also free to avoid stopping on junction
                slotSem[4].P(); //wait until junction 4 is free
                slotSem[18].P(); //wait until slot ahead of junction, slot 18, is free
                slots[4] = slots[17]; //move into junction 4
                slots[17] = "[..]"; // clear slot 17
                theTrainActivity.addMovingThroughJunction(4); // record train moving across junction
                slots[18] = slots[4]; // move into slot 18
                slots[4] = "[..]"; //clear junction 4
                slotSem[4].V(); //signal that you are leaving junction 4
                slotSem[17].V(); //signal you are leaving slot 17
                theTrainActivity.addMovedTo(18); // record that train has moved into slot 18
                currentPosition++;
            } else{
                /* wait until the position ahead is empty and then move into it*/
                slotSem[currentPosition + 1].P(); // wait for the slot ahead to be free
                slots[currentPosition + 1] = slots[currentPosition]; // move train forward
                slots[currentPosition] = "[..]"; //clear the slot the train vacated
                theTrainActivity.addMovedTo(currentPosition + 1); //record the train activity
                slotSem[currentPosition].V(); //signal slot you are leaving
                currentPosition++;
            }
        } while (currentPosition < 21 );
        CDS.idleQuietly((int) (Math.random() * 100));
    } // end trainBMoveAroundTrack

    public void trainA_MoveOffTrack(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 10));
        // record the train activity
        theTrainActivity.addMessage("Train " + trainName + " is leaving the A loop at section 0");
        slots[11] = "[..]"; // move train type A off slot 11
        slotSem[11].V();// signal slot 11 to be free
        CDS.idleQuietly((int) (Math.random() * 10));
        aCountSem.V(); // signal space for another A train
    }// end trainA_movedOffTrack

    public void trainB_MoveOffTrack(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 10));
        // record the train activity
        theTrainActivity.addMessage("Train " + trainName + " is leaving the B loop at section 16");
        slots[21] = "[..]"; // move train type A off slot 21
        slotSem[21].V();// signal slot 21 to be free
        CDS.idleQuietly((int) (Math.random() * 10));
        bCountSem.V(); // signal space for another B train
    }// end trainB_movedOffTrack
}
