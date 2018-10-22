package core;

import Objects.FileData;
import Objects.Trill;
import Objects.SPEAC;
import Objects.Measure;
import Objects.Phrase;
import Objects.Mordent;
import Objects.Note;
import Objects.Signature;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.sound.midi.*;
import javax.swing.JFileChooser;

/**
 *
 * @author MS
 */
public class EMI {

    /**
     * MIDI command value for Note On message
     */
    public static final int NOTE_ON = 0x90;

    /**
     * MIDI command value for Note Off message
     */
    public static final int NOTE_OFF = 0x80;

    /**
     * MIDI command value for End Of Track message
     */
    public static final int END_OF_TRACK = 0x2F;

    /**
     * MIDI data value for MIDI Time signature meta message
     */
    public static final int MIDI_TIME_SIGNATURE = 0x58;

    /**
     * MIDI data value for MIDI Tempo meta message
     */
    public static final int MIDI_TEMPO = 0x51;

    float divType;
    int[] timeSig;
    int res;
    int tempo;
    int maxTick;

    int[] S1 = {26, 38, 50, 62, 74, 86, 98, 30, 33, 42, 54, 66, 78, 90, 102, 45, 57, 69, 81, 93, 105};
    int[] S2 = {34, 46, 58, 70, 82, 94, 106, 26, 38, 50, 62, 74, 86, 98, 29, 41, 53, 65, 77, 89, 101};
    int[] S3 = {28, 40, 52, 64, 76, 88, 100, 32, 35, 44, 56, 68, 80, 92, 104, 47, 59, 71, 83, 95, 107};
    int[] S4 = {30, 42, 54, 66, 78, 90, 102, 25, 37, 49, 61, 73, 85, 97, 34, 46, 58, 70, 82, 94, 106};
    int[] P1 = {29, 41, 53, 65, 77, 89, 101, 33, 93, 105, 81, 45, 57, 69, 24, 36, 48, 60, 75, 84, 96, 108};
    int[] P2 = {26, 38, 50, 62, 74, 86, 98, 89, 101, 77, 65, 29, 41, 53, 93, 105, 81, 69, 33, 45, 57};
    int[] P3 = {32, 44, 56, 68, 80, 92, 104, 24, 30, 36, 48, 60, 72, 84, 96, 108, 42, 54, 66, 78, 90, 102};
    int[] P4 = {25, 37, 49, 61, 73, 85, 97, 29, 32, 41, 53, 65, 77, 89, 101, 44, 56, 68, 80, 92, 104};
    int[] E1 = {33, 45, 57, 69, 81, 93, 105, 25, 37, 49, 61, 73, 85, 97, 28, 40, 52, 64, 76, 88, 100};
    int[] E2 = {25, 37, 49, 61, 73, 85, 97, 28, 31, 34, 40, 52, 64, 76, 88, 100, 43, 55, 67, 79, 91, 103, 46, 58, 70, 82, 94, 106};
    int[] E3 = {35, 47, 59, 71, 83, 95, 107, 27, 39, 51, 63, 75, 87, 99, 30, 42, 54, 66, 78, 90, 102};
    int[] E4 = {27, 39, 51, 63, 75, 87, 99, 24, 30, 33, 36, 48, 60, 72, 84, 96, 108, 42, 54, 66, 78, 90, 102, 45, 57, 69, 81, 105};
    int[] A1 = {31, 43, 55, 67, 79, 91, 103, 35, 47, 59, 71, 83, 95, 107, 26, 38, 50, 62, 74, 86, 98, 29, 41, 53, 65, 77, 89, 101};
    int[] A2 = {35, 47, 59, 71, 83, 95, 107, 26, 38, 50, 62, 74, 86, 98, 29, 41, 53, 65, 77, 89, 101};
    int[] A3 = {32, 44, 56, 68, 80, 92, 104, 26, 29, 35, 38, 50, 62, 74, 86, 98, 41, 53, 65, 77, 89, 101, 47, 59, 71, 83, 95, 107};
    int[] A4 = {27, 39, 51, 63, 75, 87, 99, 31, 43, 55, 67, 79, 91, 103, 34, 46, 58, 70, 82, 94, 106};
    int[] C1 = {24, 36, 48, 60, 72, 84, 108, 28, 40, 52, 64, 76, 88, 100, 31, 43, 55, 67, 79, 91, 103};
    int[] C2 = {33, 45, 57, 69, 81, 93, 105, 24, 36, 48, 60, 72, 84, 96, 108, 28, 40, 52, 64, 76, 88, 102};
    int[] C3 = {24, 36, 48, 60, 72, 84, 96, 108, 28, 31, 34, 40, 52, 64, 76, 88, 100, 43, 55, 67, 79, 91, 103, 46, 58, 70, 82, 94, 106};
    int[] C4 = {28, 40, 52, 64, 76, 88, 100, 31, 43, 55, 67, 79, 91, 103, 35, 47, 59, 71, 83, 95, 107};

    int[][] weightArray = {S1, S2, S3, S4, P1, P2, P3, P4, E1, E2, E3, E4, A1, A2, A3, A4, C1, C2, C3, C4};

    ArrayList<SPEAC> speacWeights;

    //ArrayList<ArrayList<ArrayList<Measure>>> allSongs;
    /**
     *
     */
    public EMI() {
        this.speacWeights = new ArrayList<SPEAC>();
        instantiateWeights();
        //this.allSongs = new ArrayList<ArrayList<ArrayList<Measure>>>();
    }

    /**
     * This method handles the identification of each note in a song and its
     * pitch, duration, channel, velocity and on-time. This method first
     * identifies which tracks which contain notes as some MIDI files contain
     * tracks which hold data that is not useful. It then iterates through each
     * identified track looking for each note's NOTE_ON and NOTE_OFF Midi
     * Messages to extract data about each note. These notes are added to an
     * ArrayList of ArrayLists of Note objects to mirror the track-note
     * structure of a MIDI file.
     *
     * @param myMidiFile
     * @return trackArray - the ArrayList containing track-note structure
     */
    public ArrayList<ArrayList<Note>> getNotes(File myMidiFile) {
        // Read the MIDI file
        //File myMidiFile = new File(filename);
        // Obtains MIDI sequence from the file
        Sequence sequence = null;
        try {
            // Attempts to get sequence from MIDI file
            sequence = MidiSystem.getSequence(myMidiFile);
        } catch (InvalidMidiDataException ex) {
            // File that does not adhere to MIDI specification
            System.out.println("Invalid file (getnotes)");
            System.exit(0);
        } catch (IOException ex) {
            // Invalid file name
            System.out.println("File not found (getnotes)");
            System.exit(0);
        }
        // Extract the tracks containing the notes from the sequence
        Track[] tracks = sequence.getTracks();

        // An ArrayList which holds tracks that contain notes
        ArrayList<Track> noteTracks = new ArrayList<Track>();
        // The ArrayList that will hold the extracted notes
        ArrayList<ArrayList<Note>> trackArray = new ArrayList<ArrayList<Note>>();

        // Time sig and tempo are extracted within this method due to the method checking for tracks
        // that contain note data. This check establishes the framework for extracted the time sig and
        // tempo, preventing the need to create an entirely new method using the code already written
        // above and below this comment.
        // Checks if any track contains an instance of note on, if it does then the track contains notes
        // Iterate through tracks
        for (int i = 0; i < tracks.length; i++) {
            Track track = tracks[i];
            // Iterate through each message
            for (int j = 0; j < track.size(); j++) {
                MidiEvent event = track.get(j);
                MidiMessage message = event.getMessage();
                // Looks for meta message
                if (message instanceof MetaMessage) {
                    MetaMessage mm = (MetaMessage) message;
                    int type = mm.getType();
                    // Finds time signature message
                    if (type == MIDI_TIME_SIGNATURE) {
                        // Stores time signature in int array
                        timeSig = getTimeSignature(mm.getData());
                    } // Finds tempo message
                    else if (type == MIDI_TEMPO) {
                        // Stores tempo as int
                        tempo = getTempo(mm.getData());
                    }
                } // Looks for short message
                else if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    // Finds note on message
                    if (sm.getCommand() == NOTE_ON) {
                        // If note on message found then track is added to noteTracks array
                        noteTracks.add(track);
                        break;
                    }
                }
            }
        } // End of check

        // Extracts notes from tracks
        int trackNumber = 0;
        // Iterates through tracks
        for (Track track : noteTracks) {
            // Create ArrayList to hold notes
            ArrayList<Note> trackAsList = new ArrayList<Note>();

            trackArray.add(trackAsList);
            trackNumber++;
            int counter = 0;
            int nameCounter = 1;

            // Get MIDI events from tracks
            // track.size() > 0 used here as events are removed from track
            while (counter < track.size() && track.size() > 0) {
                MidiEvent event = track.get(0);
                // Get message from event
                MidiMessage message = event.getMessage();
                // Looks for short message
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    // Finds note on message
                    if (sm.getCommand() == NOTE_ON) {
                        // Get note info from message
                        int pitch = sm.getData1();
                        int velocity = sm.getData2();
                        int channel = sm.getChannel();
                        long startTime = track.get(0).getTick();
                        long endTime = 0;
                        // If velocity is zero then note off found
                        if (velocity == 0) {
                            // Remove event
                            track.remove(track.get(0));
                        } else {
                            // Iterate through the events in the track
                            for (int k = 0; k < track.size(); k++) {
                                // Looks for short message
                                if (track.get(k).getMessage() instanceof ShortMessage) {
                                    ShortMessage sm2 = (ShortMessage) (track.get(k).getMessage());
                                    // Looks for indication of note off for the previous note on event
                                    // This can be signified by either a note off event or a note on
                                    // event with velociy 0 
                                    if (sm2.getCommand() == ShortMessage.NOTE_ON) {
                                        // checks if pitch & channel match and if velocity is 0 then note off
                                        if (sm2.getData1() == pitch && sm2.getData2() == 0 && sm2.getChannel() == channel) {
                                            // Get end time of event to calculate duration
                                            endTime = track.get(k).getTick();
                                            // Create note object with extracted note information
                                            Note n = new Note(startTime, endTime - startTime, channel, velocity, pitch);
                                            // Set name for each note object
                                            n.setName("track-" + trackNumber + "-note-" + nameCounter);
                                            nameCounter++;
                                            // Add note to ArrayList
                                            trackAsList.add(n);
                                            // Remove event
                                            track.remove(track.get(0));
                                            break;
                                        }
                                    } // Looks for note off
                                    else if (sm2.getCommand() == ShortMessage.NOTE_OFF) {
                                        if (sm2.getData1() == pitch && sm2.getChannel() == channel) {
                                            // Get end time of event to calculate duration
                                            endTime = track.get(k).getTick();
                                            // Create note object with extracted note information
                                            Note n = new Note(startTime, endTime - startTime, channel, velocity, pitch);
                                            n.setName("track-" + trackNumber + "-note-" + nameCounter);
                                            nameCounter++;
                                            // Add note to ArrayList
                                            trackAsList.add(n);
                                            // Remove event
                                            track.remove(track.get(0));
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        // Remove event
                        //track.remove(track.get(0));
                    } else {
                        // Remove event
                        track.remove(track.get(0));
                    }
                } else {
                    // Remove event
                    track.remove(track.get(0));
                }
            }
        }
        return trackArray;
    }

    /**
     * The method is used to extract all relevant data from a MIDI File. Each
     * piece of information held in the MIDI file that is useful in both the
     * disassembly and analysis phases of the program is extracted. This
     * includes calling the 'disassemble' method.
     *
     * @param midiFile - the MIDI file to have data extracted from
     * @return FileData object - holds all data from the MIDI file
     */
    public FileData getFileData(File midiFile) {
        Sequence sequence = null;
        try {
            sequence = MidiSystem.getSequence(midiFile);
        } catch (InvalidMidiDataException ex) {
            System.out.println("Invalid file type (filedata) ");
            System.out.print(ex);
            System.exit(0);
        } catch (IOException ex) {
            System.out.println("File not found (filedata) " + midiFile.getAbsolutePath());
            System.out.print(ex);
            System.exit(0);
        }
        // Initialise variables needed to create FileData object
        long tickLength = 0;
        long microsecondLength = 0;
        float divisionType = 0;
        int resolution = 0;
        // Gets data from sequence
        if (sequence != null) {
            tickLength = sequence.getTickLength();
            microsecondLength = sequence.getMicrosecondLength();
            divisionType = sequence.getDivisionType();
            resolution = sequence.getResolution();
        }
        // Division type used to determine sequence formatting
        divType = divisionType;
        // Ticks per beat
        res = resolution;
        String name = midiFile.getName();
        FileData fileData = new FileData(name, tickLength, microsecondLength, divisionType, resolution);
        // Create track-note structure
        ArrayList<ArrayList<Note>> noteArray = getNotes(midiFile);
        fileData.setNoteArray(getNotes(midiFile));
        fileData.setClarified(noteArray);
        // Set time signature
        fileData.setTimeSig(timeSig);
        maxTick = res * timeSig[0];      // NEEDS TO BE FIXED THIS VALUE IS SOMEWHAT ARBITRARY   res + (res / 2);
        return fileData;
    }

    /**
     * Converts the raw time signature from a byte array to an integer array.
     *
     * @param byteTimeSig - time signature encoded into a byte array
     * @return the time signature as an integer array
     */
    public int[] getTimeSignature(byte[] byteTimeSig) {
        int[] intTimeSig = new int[4];
        if (byteTimeSig.length == 4) {
            for (int i = 0; i < byteTimeSig.length; i++) {
                intTimeSig[i] = byteTimeSig[i];
            }
        } else {
            System.out.println("Incorrect meta message identified as time signature.");
        }
        return intTimeSig;
    }

    /**
     * Used to extract integer value for tempo. The tempo is stored as a meta
     * message. To get the tempo the data from the meta message is extracted as
     * a byte array. The values from the byte array need to be concatenated and
     * then converted from a hexadecimal value to an integer. This value is then
     * used to divide 60,000,000 to get a value in bpm (beats per minute).
     *
     * @param byteTempo - the byte array containing the tempo
     * @return the tempo as an integer in bpm
     */
    public int getTempo(byte[] byteTempo) {
        StringBuilder sb = new StringBuilder();
        for (byte b : byteTempo) {
            sb.append(String.format("%02X", b));
        }
        int tempo = Integer.valueOf(sb.toString(), 16);
        int bpm = 60000000 / tempo;
        return bpm;
    }

    /**
     * Divides the all of the notes from the track-note structure into phrases
     * to mirror the bar notation common in sheet music.
     *
     * @param trackArray
     * @return
     */
    public ArrayList<ArrayList<Measure>> getMeasures(ArrayList<ArrayList<Note>> trackArray) {
        ArrayList<ArrayList<Measure>> measureArray = new ArrayList<ArrayList<Measure>>();
        for (int i = 0; i < trackArray.size() - 1; i++) {
            ArrayList<Note> track = trackArray.get(i);
            ArrayList<Measure> trackMeasures = new ArrayList<Measure>();
            ArrayList<Note> measureNotes = new ArrayList<Note>();
            int measuresMade = 1;
            int check = maxTick;
            for (int j = 0; j < track.size() - 1; j++) {
                int start = (int) track.get(j).getStart();
                if (start - check < 0) {
                    measureNotes.add(track.get(j));
                } else if (start - check >= 0) {
                    Measure measure = new Measure(measureNotes);
                    trackMeasures.add(measure);
                    measuresMade++;
                    check = maxTick * measuresMade;
                    measureNotes = new ArrayList<Note>();
                    measureNotes.add(track.get(j));
                }
            }
            measureArray.add(trackMeasures);
        }
        return measureArray;
    }

    /**
     * Find measures groups together measures that exist across all tracks
     * within a song.
     *
     * @param measures - the list of measures held in a track-note structure
     * @return an ArrayList of measures containing all notes from the song.
     */
    public ArrayList<Measure> findChords(ArrayList<ArrayList<Measure>> measures) {
        ArrayList<Measure> chords = new ArrayList<Measure>();
        // iterate across the first track
        for (int j = 0; j < measures.get(0).size(); j++) {
            long start = 0;
            // ignore measures that contain no notes
            if (measures.get(0).get(j).getMeasureNotes() != null && !measures.get(0).get(j).getMeasureNotes().isEmpty()) {
                // get start time of measure to find appropriate measure in other tracks
                start = measures.get(0).get(j).getMeasureNotes().get(0).getStart();
            }
            Measure curr = measures.get(0).get(j);
            ArrayList<Note> notes = new ArrayList<Note>();
            notes.addAll(curr.getMeasureNotes());
            // iterate through tracks excluding the first track
            for (int k = 1; k < measures.size(); k++) {
                // iterate through measures within track
                for (int i = 0; i < measures.get(k).size(); i++) {
                    // find measure with same start time as initial measure, ignoring measures that contain no notes
                    if (measures.get(k).get(i).getMeasureNotes() != null && !measures.get(k).get(i).getMeasureNotes().isEmpty() && measures.get(k).get(i).getMeasureNotes().get(0).getStart() == start) {
                        notes.addAll(measures.get(k).get(i).getMeasureNotes());
                    }
                }
            }
            // create new measure to hold all notes
            Measure newMeasure = new Measure(notes);
            chords.add(newMeasure);
        }

        return chords;
    }

    /**
     * Determines the pattern for each measure passed to it for the pattern
     * matching process. The pattern is determined by calculating the difference
     * in pitch between a note and the note ahead of it. For example, a measure
     * with pitch values (60, 62, 64) would equate to (2, 2).
     *
     * @param measureArray - the measures that will have their patterns
     * calculated.
     * @return the measures array passed to the method but with patterns applied
     * to their respective measures
     */
    public ArrayList<Measure> setPatterns(ArrayList<Measure> measureArray) {
        for (Measure m : measureArray) {
            ArrayList<Note> notes = m.getMeasureNotes();
            int[] pattern;
            if (notes.size() <= 1) {
                continue;
            } else {
                pattern = new int[notes.size() - 2];
            }
            for (int i = 0; i < notes.size() - 2; i++) {
                pattern[i] = notes.get(i + 1).getPitch() - notes.get(i).getPitch();
            }
            m.setPattern(pattern);
        }

        return measureArray;
    }

    /**
     * Concatenate measures into a specified size for the pattern matching
     * process. Being passed 2 as input, for example, results in every two
     * measures being concatenated together
     *
     * @param measures - the measure to be concatenated
     * @param patternSize - the number of measures to be concatenated together
     * @return the resulting ArrayList of measures
     */
    public ArrayList<Measure> setPatternSize(ArrayList<Measure> measures, int patternSize) {
        ArrayList<Measure> track = new ArrayList<Measure>();
        ArrayList<Note> notes = new ArrayList<Note>();
        // iterate through measures
        for (int j = 0; j < measures.size() - 1; j++) {
            notes.addAll(measures.get(j).getMeasureNotes());
            if ((j + 1) % patternSize == 0) {
                Measure m = new Measure(notes);
                track.add(m);
                notes = new ArrayList<Note>();
            } // ignore remaining measures at the end of the song
            else if ((j + 1) % patternSize != 0 && ((measures.size() - 1) - (j + 1)) < patternSize) {
                Measure m = new Measure(notes);
                track.add(m);
                notes = new ArrayList<Note>();
            }
        }

        return track;
    }

    /**
     * Calls all relevant methods to prepare song for the pattern matching
     * process.
     *
     * @param patternSize - the number of measures to form a pattern
     * @param filedata - the song to be analysed
     * @return
     */
    public FileData prepareForPatternMatching(int patternSize, FileData filedata) {
        // gets the song as measures
        ArrayList<Measure> measuresForMatching = filedata.getSongAsSingleTrack();
        measuresForMatching = setPatternSize(measuresForMatching, patternSize);
        measuresForMatching = transposeToCMajor(measuresForMatching);
        measuresForMatching = setPatterns(measuresForMatching);
        filedata.setMeasuresForMatching(measuresForMatching);
        return filedata;
    }

    /**
     * The pattern matching process. This is method performs the pattern
     * matching. The variables taken by this method have differing degrees of
     * effect on the output of the method. The method checks to see if the
     * current pattern and a potential pattern are of the same length, or
     * similar to the extent defined by the value of the lengthAllowance
     * variable. If the potential match meets this criteria, the two patterns
     * are cross examined by their interval (difference in pitch) and direction
     * (up or down i.e. positive or negative). A weight is created for both of
     * these which is representative of the similarity between the two patterns
     * in both of these regards. These two weights are then used to determine
     * whether or not the the patterns match, by comparing the result of the two
     * weights against the threshold specified by the threshold variable. A
     * pattern that supersedes the threshold is identified as a signature and is
     * added to the filedata of the appropriate song.
     *
     * @param fileDataList - the list of songs have their signatures extracted
     * @param patternSize - the number of measures to form a pattern
     * @param benchmark - the minimum % required for a pattern to be assigned a
     * positive weight (patterns that fall below the benchmark are assigned a
     * negative weight)
     * @param threshold - the minimum % required for a pattern to be identified
     * as a signature
     * @param intervalAllowance - the maximum difference between two patterns
     * respective pitches
     * @param lengthAllowance - the maximum difference in length between two
     * patterns
     * @return - the list of songs with signatures extracted (or not)
     */
    public ArrayList<FileData> matchPatterns(ArrayList<FileData> fileDataList, int patternSize, int benchmark, int threshold, int intervalAllowance, int lengthAllowance) {
        // iterate through songs
        for (FileData filedata : fileDataList) {
            // call methods to prepare for matching
            filedata = prepareForPatternMatching(patternSize, filedata);
            ArrayList<Measure> measuresForMatching = filedata.getMeasuresForMatching();
            currm:  // current measure loop
            for (int m = 0; m < measuresForMatching.size() - 1; m++) {
                nextm:  // next measure loop
                for (int n = 1; n < measuresForMatching.size() - 1; n++) {
                    Measure currMeasure = measuresForMatching.get(m);
                    Measure nextMeasure = measuresForMatching.get(n);
                    // store patterns
                    int[] curr = currMeasure.getPattern();
                    int[] next = nextMeasure.getPattern();
                    // ignore empty measures
                    if (curr == null || curr.length == 0) {
                        continue currm;
                    } else if (next == null || next.length == 0) {
                        continue nextm;
                    }
                    // m != n stops the current measure being matched with itself
                    if (Arrays.equals(curr, next) && m != n) {
                        // unimplemented feature
                        currMeasure.incrementMatchedPatterns();
                    } else {
                        // if same length
                        if (curr.length == next.length && m != n) {
                            int weight1 = 0;
                            int weight2 = 0;
                            float currLength = curr.length;
                            // calculate minimum for pattern to pass tests
                            float currThreshold = (currLength / 10) * (benchmark / 10);
                            // INTERVAL CHECK
                            // compare using absolute values
                            int[] absCurr = curr;
                            int[] absNext = next;
                            for (int i = 0; i < curr.length; i++) {
                                absCurr[i] = Math.abs(absCurr[i]);
                                absNext[i] = Math.abs(absNext[i]);
                            }
                            // find number of similar intervals
                            int[] intDiff = new int[curr.length];
                            for (int i = 0; i < absCurr.length; i++) {
                                // if interval difference is greater than the allowance then discard
                                if (absCurr[i] - absNext[i] > intervalAllowance || absCurr[i] - absNext[i] < -intervalAllowance) {
                                    continue nextm;
                                } else {
                                    // store interval differences
                                    intDiff[i] = absCurr[i] - absNext[i];
                                }
                            }
                            // get total number of non similar intervals
                            int totalIntDiff = 0;
                            for (int i = 0; i < intDiff.length; i++) {
                                if (intDiff[i] != 0) {
                                    totalIntDiff++;
                                }
                            }
                            // if total similar greater than threshold then generate first weight
                            //if (curr.length - totalIntDiff > currThreshold) {
                            float a = curr.length - totalIntDiff;
                            float b = curr.length;
                            weight1 = (int) ((a / b) * 100);
                            //}

                            //DIRECTION CHECK
                            int totalDirDiff = 0;
                            // checks if intervals match
                            for (int i = 0; i < curr.length; i++) {
                                // uses sign funtion to get the sign of a number
                                if (Math.signum(curr[i]) != Math.signum(next[i])) {
                                    totalDirDiff++;
                                }
                            }
                            // if total similar greater than threshold generate second weight
                            //if (curr.length - totalDirDiff > currThreshold) {
                            float c = curr.length - totalDirDiff;
                            float d = curr.length;
                            weight2 = (int) ((c / d) * 100);
                            //}
                            // use summational method to calculate result
                            int result = ((weight1 - benchmark) + (weight2 - benchmark));
                            // if result is above threshold then a signature has been found
                            if (result >= threshold) {
                                // store signature in signature object
                                Signature sig = new Signature(currMeasure.getMeasureNotes(), filedata.getName());
                                filedata.addSignature(sig);
                                currMeasure.incrementMatchedPatterns();
                            }
                        } // if next length is smaller
                        else if (curr.length - next.length <= lengthAllowance && curr.length > next.length && m != n) {
                            int weight1 = 0;
                            int weight2 = 0;
                            float length = next.length;
                            float currThreshold = (length / 10) * (benchmark / 10);

                            int[] absCurr = curr;
                            int[] absNext = next;
                            for (int i = 0; i < next.length; i++) {
                                absCurr[i] = Math.abs(absCurr[i]);
                                absNext[i] = Math.abs(absNext[i]);
                            }
                            int[] intDiff = new int[next.length];
                            for (int i = 0; i < absNext.length; i++) {
                                if (absCurr[i] - absNext[i] > intervalAllowance || absCurr[i] - absNext[i] < -intervalAllowance) {
                                    continue nextm;
                                } else {
                                    intDiff[i] = absCurr[i] - absNext[i];
                                }
                            }
                            int totalIntDiff = 0;
                            for (int i = 0; i < intDiff.length; i++) {
                                if (intDiff[i] != 0) {
                                    totalIntDiff++;
                                }
                            }
                            //if (next.length - totalIntDiff > currThreshold) {
                            float a = next.length - totalIntDiff;
                            float b = next.length;
                            weight1 = (int) ((a / b) * 100);
                            //}

                            int totalDirDiff = 0;
                            // checks if patterns are different
                            for (int i = 0; i < next.length; i++) {
                                if (Math.signum(curr[i]) != Math.signum(next[i])) {
                                    totalDirDiff++;
                                }
                            }
                            //if (next.length - totalDirDiff > currThreshold) {
                            float c = next.length - totalDirDiff;
                            float d = next.length;
                            weight2 = (int) ((c / d) * 100);
                            //}
                            int result = ((weight1 - benchmark) + (weight2 - benchmark));
                            if (result >= threshold) {
                                Signature sig = new Signature(currMeasure.getMeasureNotes(), filedata.getName());
                                filedata.addSignature(sig);
                                currMeasure.incrementMatchedPatterns();
                            }
                        } // if curr length is smaller
                        else if (curr.length - next.length == -lengthAllowance && curr.length < next.length && m != n) {
                            int weight1 = 0;
                            int weight2 = 0;
                            float currLength = curr.length;
                            float currThreshold = (currLength / 10) * (benchmark / 10);

                            int[] absCurr = curr;
                            int[] absNext = next;
                            for (int i = 0; i < curr.length; i++) {
                                absCurr[i] = Math.abs(absCurr[i]);
                                absNext[i] = Math.abs(absNext[i]);
                            }
                            int[] intDiff = new int[curr.length];
                            for (int i = 0; i < absCurr.length; i++) {
                                if (absCurr[i] - absNext[i] > intervalAllowance || absCurr[i] - absNext[i] < -intervalAllowance) {
                                    continue nextm;
                                } else {
                                    intDiff[i] = absCurr[i] - absNext[i];
                                }
                            }
                            int totalIntDiff = 0;
                            for (int i = 0; i < intDiff.length; i++) {
                                if (intDiff[i] != 0) {
                                    totalIntDiff++;
                                }
                            }
                            //if (curr.length - totalIntDiff > currThreshold) {
                            float a = curr.length - totalIntDiff;
                            float b = curr.length;
                            weight1 = (int) ((a / b) * 100);
                            //}

                            int totalDirDiff = 0;
                            // checks if patterns are different
                            for (int i = 0; i < curr.length; i++) {
                                if (Math.signum(curr[i]) != Math.signum(next[i])) {
                                    totalDirDiff++;
                                }
                            }
                            //if (curr.length - totalDirDiff > currThreshold) {
                            float c = curr.length - totalDirDiff;
                            float d = curr.length;
                            weight2 = (int) ((c / d) * 100);
                            //}
                            int result = ((weight1 - benchmark) + (weight2 - benchmark));
                            if (result >= threshold) {
                                Signature sig = new Signature(currMeasure.getMeasureNotes(), filedata.getName());
                                filedata.addSignature(sig);
                                currMeasure.incrementMatchedPatterns();
                            }
                        }
                    }
                }
            }
        }
        return fileDataList;
    }

    /**
     * Transposes all notes from a song into the key of C Major.
     *
     * @param measureArray - the song to be transposed
     * @return - the transposed song
     */
    public ArrayList<Measure> transposeToCMajor(ArrayList<Measure> measureArray) {
        // octave number for C Major
        int cMajorOctave = 4;
        // iterate through measures
        for (Measure m : measureArray) {
            //iterate through notes
            for (Note n : m.getMeasureNotes()) {
                int pitch = n.getPitch();
                // calculate difference in octaves
                int octaveDiff = cMajorOctave - (pitch / 12);
                // if octave same then note already in C Major octave
                if (octaveDiff == cMajorOctave) {
                    break;
                } // adjust pitch
                else {
                    int pitchDiff = octaveDiff * 12;
                    pitch = pitch + pitchDiff;
                    n.setPitch(pitch);
                }
            }
        }

        return measureArray;
    }

    /**
     * Remove all trill ornaments from a song. This method identifies instances
     * of trills within a song and replaces them with a single note. Strategic
     * indexing has been utilised in this method to emulate the ability to 'look
     * ahead'.
     *
     * @param f - the FileData object for the song
     * @return - the song with trills removed
     */
    public FileData removeTrills(FileData f) {
        // get clarified song data
        ArrayList<ArrayList<Note>> noteArray = f.getClarified();
        // iterate through tracks
        for (ArrayList<Note> track : noteArray) {
            // iterate through track notes
            // for each loop not used as removal of objects from the list
            // being iterated through is not allowed
            for (int i = 0; i < track.size() - 1; i++) {
                // termination case
                if (i == track.size() - 2) {
                    break;
                }
                Note startNote = track.get(i);
                Note nextNote = track.get(i + 1);
                int end = 0;
                // check for second note of a trill where difference between intervals is 1
                if (nextNote.getPitch() == startNote.getPitch() + 1 || nextNote.getPitch() == startNote.getPitch() - 1) {
                    int diff = nextNote.getPitch() - startNote.getPitch();
                    // different operations required depending upon whether the pitch
                    // of the second note of the trill is above or below the first
                    boolean done = false;
                    boolean sub = false;
                    if (diff == -1) {
                        sub = true;
                    } else if (diff == 1) {
                        sub = false;
                    }
                    int noteCount = 2;
                    Note prevNote = nextNote;
                    // not sure how many times loop needs to be run so while 
                    // loop finds all remaining notes of trill
                    while (!done) {
                        // termination case
                        if (i + noteCount == track.size()) {
                            break;
                        }
                        nextNote = track.get(i + noteCount);
                        // if current note higher than previous
                        if (sub == false) {
                            // checks if next interval is lower than current
                            if (nextNote.getPitch() == prevNote.getPitch() - 1) {
                                sub = true;
                                prevNote = nextNote;
                            } else {
                                // if 4 or more notes found then trill identified
                                if (noteCount > 3) {
                                    // store index of last note of trill
                                    end = i + noteCount;
                                }
                                // terminate loop
                                done = true;
                            }
                        } // if current note lower than previous
                        else if (sub == true) {
                            // checks if next interval is higher than current
                            if (nextNote.getPitch() == prevNote.getPitch() + 1) {
                                sub = false;
                                prevNote = nextNote;
                            } else {
                                // if four or more notes found then trill found
                                if (noteCount > 3) {
                                    // store index of last note of trill
                                    end = i + noteCount;
                                }
                                // terminate loop
                                done = true;
                            }
                        }
                        noteCount++;
                    }
                } // check for second note of a trill where difference between intervals is 2
                else if (nextNote.getPitch() == startNote.getPitch() + 2 || nextNote.getPitch() == startNote.getPitch() - 2) {
                    int diff = nextNote.getPitch() - startNote.getPitch();
                    boolean done = false;
                    boolean sub = false;
                    if (diff == -2) {
                        sub = true;
                    } else if (diff == 2) {
                        sub = false;
                    }
                    int noteCount = 2;
                    Note prevNote = nextNote;
                    // not sure how many times loop needs to be run so while 
                    while (!done) {
                        if (i + noteCount == track.size()) {
                            break;
                        }
                        nextNote = track.get(i + noteCount);
                        if (sub == false) {
                            if (nextNote.getPitch() == prevNote.getPitch() - 2) {
                                sub = true;
                                prevNote = nextNote;
                            } else {
                                if (noteCount > 3) {
                                    end = i + noteCount;
                                }
                                done = true;
                            }
                        } else if (sub == true) {
                            if (nextNote.getPitch() == prevNote.getPitch() + 2) {
                                sub = false;
                                prevNote = nextNote;
                            } else {
                                if (noteCount > 3) {
                                    end = i + noteCount;
                                }
                                done = true;
                            }
                        }
                        noteCount++;
                    }
                }
                // if trill found
                if (end != 0) {
                    // create Trill object to store identified trill
                    Trill trill = new Trill();
                    long duration = 0;
                    // add all trill notes to Trill object and calculate total duration of trill
                    for (int j = i; j < end; j++) {
                        trill.addNoteToTrill(track.get(j));
                        duration += track.get(j).getDuration();
                    }
                    // remove trill notes from song
                    for (int j = 0; j < (end) - i; j++) {
                        track.remove(i);
                    }
                    // create new note to replace trill and add to song
                    Note n = new Note(startNote.getStart(), duration, startNote.getChannel(), startNote.getVelocity(), startNote.getPitch());
                    track.add(i, n);
                    f.addTrill(trill);
                }
            }
        }
        return f;
    }

    /**
     * Remove all acciaccaturas (grace note) from song. Finds any instance of an
     * acciaccatura in a song and removes them without replacement.
     *
     * @param f - the FileData object for the song
     * @return - the song with acciaccaturas removed
     */
    public FileData removeAcciaccaturas(FileData f) {
        // get clarifed song data
        ArrayList<ArrayList<Note>> noteArray = f.getClarified();
        // iterate through tracks
        for (ArrayList<Note> track : noteArray) {
            // iterate through track notes
            for (int i = 0; i < track.size() - 1; i++) {
                // termination case
                if (i + 1 == track.size() - 1) {
                    break;
                }
                Note currNote = track.get(i);
                Note nextNote = track.get(i + 1);
                // looks for notes of extremely short duration that occur at the same time as another note
                if (currNote.getStart() == nextNote.getStart() && currNote.getDuration() < 10
                        || currNote.getStart() == nextNote.getStart() && nextNote.getDuration() < 10) {
                    Note acc = null;
                    // determine whether the next note or current note is the acciaccatura
                    if (currNote.getDuration() < nextNote.getDuration()) {
                        acc = currNote;
                    } else {
                        acc = nextNote;
                    }
                    // remove acciaccatura from song
                    f.getAccArray().add(acc);
                    track.remove(acc);
                }
            }
        }
        return f;
    }

    /**
     * Remove instances of appoggiaturas (grace note) from song. This method has
     * not be fully implemented.
     *
     * @param f - the FileData object for the song
     * @return - the song with appoggiaturas removed
     */
    public FileData removeAppoggiaturas(FileData f) {
        // get clarified song data
        ArrayList<ArrayList<Note>> noteArray = f.getClarified();
        // iterate through tracks
        for (ArrayList<Note> track : noteArray) {
            int notecount = 0;
            // iterate through notes
            for (int i = 0; i < track.size(); i++) {
                if (i == track.size() - 1) {
                    break;
                }
                notecount++;
                Note currNote = track.get(i);
                Note nextNote = track.get(notecount);
                if (true) {
                    // needs to be completed
                }
            }
        }

        return f;
    }

    /**
     * Remove all instances of mordent ornaments in a song. The method finds all
     * mordent ornaments in a song and replaces them with a single note
     * utilising strategic indexing to 'look ahead' to find ornament notes.
     *
     * @param f - the FileData object for the song
     * @return - the song with mordents removed
     */
    public FileData removeMordents(FileData f) {
        // get clarified song data
        ArrayList<ArrayList<Note>> noteArray = f.getClarified();
        // iterate through tracks
        for (ArrayList<Note> track : noteArray) {
            // iterate through notes
            for (int i = 0; i < track.size() - 1; i++) {
                // termination case
                if (i == track.size() - 2) {
                    break;
                }
                Note currNote = track.get(i);
                Note possMordent = track.get(i + 1);
                Note endNote = track.get(i + 2);
                // determine duration of potential last note of mordent
                long endDur = endNote.getDuration() / 2;
                // if first note and last note of potential mordent are the same 
                // and the duration of the last note is less than the determined duration
                if (currNote.getPitch() == endNote.getPitch()
                        && currNote.getDuration() <= endDur
                        && possMordent.getDuration() <= endDur) {
                    // if pitch of next note meets condition then mordent found
                    if (possMordent.getPitch() == currNote.getPitch() + 1
                            || possMordent.getPitch() == currNote.getPitch() - 1
                            || possMordent.getPitch() == currNote.getPitch() + 2
                            || possMordent.getPitch() == currNote.getPitch() - 2) {
                        // create Mordent object to store mordent
                        Mordent mordent = new Mordent();
                        mordent.addNoteToMordent(currNote);
                        mordent.addNoteToMordent(possMordent);
                        mordent.addNoteToMordent(endNote);
                        f.getMordentArray().add(mordent);
                        // create new note to replace mordent
                        long duration = currNote.getDuration() + possMordent.getDuration() + endNote.getDuration();
                        Note newNote = new Note(currNote.getStart(), duration, currNote.getChannel(), currNote.getVelocity(), currNote.getPitch());
                        // remove mordent from track
                        track.remove(currNote);
                        track.remove(possMordent);
                        track.remove(endNote);
                        track.add(newNote);
                    }
                }
            }
        }
        return f;
    }

    /**
     * Remove any note from a song that is longer than twice the PPQ.
     *
     * @param f - the FileData object for the song
     * @return - the song with long notes removed
     */
    public FileData removeLongNotes(FileData f) {
        // get clarified data
        ArrayList<ArrayList<Note>> noteArray = f.getClarified();
        // iterate through tracks
        for (ArrayList<Note> track : noteArray) {
            // iterate through notes
            for (int i = 0; i < track.size() - 1; i++) {
                Note currNote = track.get(i);
                // determine duration of note and remove if condition met
                if (currNote.getDuration() >= res * 2) {
                    track.remove(i);
                }
            }
        }
        return f;
    }

    /**
     * Determine which forms of data clarification to perform (if any) on a
     * song. Boolean values received from user input to determine which
     * clarification to perform on the song.
     *
     * @param f - the FileData object for the song
     * @param mordents - if true then mordents removed
     * @param grace - if true then grace notes removed
     * @param trills - if true then trills removed
     * @param longs - if true then long notes removed
     */
    public void clarifyData(FileData f, boolean mordents, boolean grace, boolean trills, boolean longs) {
        if (mordents) {
            f = removeMordents(f);
        }
        if (grace) {
            f = removeAcciaccaturas(f);
            f = removeAppoggiaturas(f);
        }
        if (trills) {
            f = removeTrills(f);
        }
        if (longs) {
            f = removeLongNotes(f);
        }
    }

    /**
     * Prepare default SPEAC settings for analysis.
     */
    public void instantiateWeights() {
        ArrayList<SPEAC> speacArray = new ArrayList<SPEAC>();
        int num = 1;
        for (int i = 0; i < weightArray.length; i++) {
            if (num == 5) {
                num = 1;
            }
            if (i < 4) {
                String name = "s" + num;
                SPEAC weight = new SPEAC(weightArray[i], name);
                speacArray.add(weight);
                num++;
            } else if (i < 8) {
                String name = "p" + num;
                SPEAC weight = new SPEAC(weightArray[i], name);
                speacArray.add(weight);
                num++;
            } else if (i < 12) {
                String name = "e" + num;
                SPEAC weight = new SPEAC(weightArray[i], name);
                speacArray.add(weight);
                num++;
            } else if (i < 16) {
                String name = "a" + num;
                SPEAC weight = new SPEAC(weightArray[i], name);
                speacArray.add(weight);
                num++;
            } else if (i < 20) {
                String name = "c" + num;
                SPEAC weight = new SPEAC(weightArray[i], name);
                speacArray.add(weight);
                num++;
            }
        }
        speacWeights = speacArray;
    }

    /**
     * Apply SPEAC analysis on measures. This method iterates through all
     * measures and determines the most suitable identifier for almost all
     * measures. Measures containing no notes are left unclassified.
     *
     * @param measureArray - the measures to be classified
     * @return - all measures containing notes labelled with the appropriate
     * SPEAC identifier
     */
    public ArrayList<Measure> setSpeacMeasures(ArrayList<Measure> measureArray) {
        // iterate through measures
        for (Measure m : measureArray) {
            int[] pitchValues = m.getPitchValues();
            // if no notes then skip
            if (pitchValues == null) {
                continue;
            }
            int best = 0;
            int bestc = 0;
            int count = 0;
            // iterate through identifiers
            for (SPEAC s : speacWeights) {
                int matched = 0;
                // iterate through pitch values for measure
                for (int pitch : pitchValues) {
                    // iterate through each individual setting for within the identifier
                    for (int value : s.getWeight()) {
                        // if match found then increment
                        if (pitch == value) {
                            matched++;
                            break;
                        }
                    }
                }
                // on first iteration first setting set as best to prepare for comparison
                if (best == 0) {
                    best = matched;
                }
                // if current matched values for identifer greater than previous best match then replace
                if (matched > best) {
                    best = matched;
                    bestc = count;
                }
                count++;
            }
            // apply best match to measure
            m.setSpeacWeight(speacWeights.get(bestc).getName());
        }

        return measureArray;
    }

    /**
     * Apply SPEAC analysis on measures. This method differs slightly from the
     * previous SPEAC analysis method by implementing special conditions for
     * certain identifers.
     *
     * @param phraseArray - the phrases to be classified
     * @return - all phrases appropriately classified
     */
    public ArrayList<Phrase> setSpeacPhrases(ArrayList<Phrase> phraseArray) {
        // the three identifiers requiring special cases
        int a1 = 0;
        int e4 = 0;
        int c3 = 0;
        // iterate through phrases
        for (Phrase p : phraseArray) {
            int[] pitchValues = p.getPitchValues();
            // if no notes then skip
            if (pitchValues == null) {
                continue;
            }
            int best = 0;
            int bestc = 0;
            int count = 0;
            for (SPEAC s : speacWeights) {
                int matched = 0;
                for (int pitch : pitchValues) {
                    for (int value : s.getWeight()) {
                        if (pitch == value) {
                            matched++;
                            break;
                        }
                    }
                }
                if (best == 0) {
                    best = matched;
                }
                if (matched > best) {
                    best = matched;
                    bestc = count;
                }
                count++;
            }
            // increment number of instances of special cases created
            if (bestc == 11) {
                e4++;
            } else if (bestc == 12) {
                a1++;
            } else if (bestc == 18) {
                c3++;
            }
            // convert every third a1 to s1
            if (a1 == 3) {
                bestc = 0;
                a1++;
            } // convert every sixth a1 to s2
            else if (a1 == 6) {
                bestc = 1;
                a1 = 0;
            }
            // convert every third c3 to s3
            if (c3 == 3) {
                bestc = 2;
                c3++;
            } // convert every sixth c3 to s2
            else if (c3 == 6) {
                bestc = 1;
                c3 = 0;
            }
            // convert every third e4 to s4
            if (e4 == 3) {
                bestc = 3;
                c3++;
            } // convert every sixth e4 to s1
            else if (e4 == 6) {
                bestc = 0;
                e4 = 0;
            }
            // apply identifer to phrase
            p.setSpeacWeight(speacWeights.get(bestc).getName());
        }
        return phraseArray;
    }

    /**
     * Sorts all measures from song by their respective SPEAC identifier. This
     * method prepares each song for the recombination process by organising the
     * measures into an appropriate manner for the ATN.
     *
     * @param measureArray - the measures of the song
     * @return - the measures sorted by their identifier
     */
    public ArrayList<ArrayList<Measure>> sortMeasuresBySpeac(ArrayList<Measure> measureArray) {
        // prepare list for measures
        ArrayList<ArrayList<Measure>> measuresBySpeac = new ArrayList<ArrayList<Measure>>();
        ArrayList<Measure> S = new ArrayList<Measure>();
        ArrayList<Measure> P = new ArrayList<Measure>();
        ArrayList<Measure> E = new ArrayList<Measure>();
        ArrayList<Measure> A = new ArrayList<Measure>();
        ArrayList<Measure> C = new ArrayList<Measure>();
        measuresBySpeac.add(S);
        measuresBySpeac.add(P);
        measuresBySpeac.add(E);
        measuresBySpeac.add(A);
        measuresBySpeac.add(C);
        // iterate through measures
        for (Measure m : measureArray) {
            String weight = m.getSpeacWeight();
            // get identifier and store appropriately, or skip if null
            if (weight == null) {
                continue;
            } else if (weight.startsWith("s")) {
                measuresBySpeac.get(0).add(m);
            } else if (weight.startsWith("p")) {
                measuresBySpeac.get(1).add(m);
            } else if (weight.startsWith("e")) {
                measuresBySpeac.get(2).add(m);
            } else if (weight.startsWith("a")) {
                measuresBySpeac.get(3).add(m);
            } else if (weight.startsWith("c")) {
                measuresBySpeac.get(4).add(m);
            }
        }
        return measuresBySpeac;
    }

    /**
     * Sorts phrases by their SPEAC identifier in preparation for recombination
     * into a new song.
     *
     * @param phraseArray - the phrases generated by the ATN
     * @return - the phrases sorted by their respective identifier
     */
    public ArrayList<ArrayList<Phrase>> sortPhrasesBySpeac(ArrayList<Phrase> phraseArray) {
        ArrayList<ArrayList<Phrase>> phrasesBySpeac = new ArrayList<ArrayList<Phrase>>();
        ArrayList<Phrase> S = new ArrayList<Phrase>();
        ArrayList<Phrase> P = new ArrayList<Phrase>();
        ArrayList<Phrase> E = new ArrayList<Phrase>();
        ArrayList<Phrase> A = new ArrayList<Phrase>();
        ArrayList<Phrase> C = new ArrayList<Phrase>();
        phrasesBySpeac.add(S);
        phrasesBySpeac.add(P);
        phrasesBySpeac.add(E);
        phrasesBySpeac.add(A);
        phrasesBySpeac.add(C);
        for (Phrase p : phraseArray) {
            String weight = p.getSpeacWeight();
            if (weight == null) {
                continue;
            } else if (weight.startsWith("s")) {
                phrasesBySpeac.get(0).add(p);
            } else if (weight.startsWith("p")) {
                phrasesBySpeac.get(1).add(p);
            } else if (weight.startsWith("e")) {
                phrasesBySpeac.get(2).add(p);
            } else if (weight.startsWith("a")) {
                phrasesBySpeac.get(3).add(p);
            } else if (weight.startsWith("c")) {
                phrasesBySpeac.get(4).add(p);
            }
        }
        return phrasesBySpeac;
    }

    /**
     * Creates a new SPEAC object containing a new setting which holds a
     * definition of the tonic, dominant and subdominant actors.
     *
     * @param name - the name of the new setting
     * @param tonic
     * @param dominant
     * @param subdominant
     * @return - a SPEAC object containing the new setting
     */
    public SPEAC addSetting(String name, int tonic, int dominant, int subdominant) {
        // int array to hold individual setting
        int[] newSetting = new int[21];
        // generates values for setting
        for (int i = 0; i <= 7; i++) {
            // strategic indexing used to fill array simultaneously
            int nextTonic = tonic + ((i * 1) * 12);
            int nextDom = dominant + ((i * 1) * 12);
            int nextSub = subdominant + ((i * 1) * 12);
            // if value outside MIDI range entered then adjust
            if (Math.signum(tonic) >= 0) {
                while (nextTonic > 127) {
                    nextTonic = nextTonic - 128;
                }
            } else if (Math.signum(nextTonic) < 0) {
                while (nextTonic < 127) {
                    nextTonic = nextTonic + 128;
                }
            }
            if (Math.signum(nextDom) >= 0) {
                while (nextDom > 127) {
                    nextDom = nextDom - 128;
                }
            } else if (Math.signum(nextDom) < 0) {
                while (nextDom < 127) {
                    nextDom = nextDom + 128;
                }
            }
            if (Math.signum(nextSub) >= 0) {
                while (nextSub > 127) {
                    nextSub = nextSub - 128;
                }
            } else if (Math.signum(nextSub) < 0) {
                while (nextSub < 127) {
                    nextSub = nextSub + 128;
                }
            }
            newSetting[i] = nextTonic;
            newSetting[i + 6] = nextDom;
            newSetting[i + 13] = nextSub;
        }
        return new SPEAC(newSetting, name);
    }

    /**
     * Helper method for other methods related to setting creation.
     *
     * @param tonic
     * @param dominant
     * @param subdominant
     * @return
     */
    public int[] addSetting(int tonic, int dominant, int subdominant) {
        int[] newSetting = new int[21];
        for (int i = 0; i <= 7; i++) {
            int nextTonic = tonic + ((i * 1) * 12);
            int nextDom = dominant + ((i * 1) * 12);
            int nextSub = subdominant + ((i * 1) * 12);
            if (Math.signum(tonic) >= 0) {
                while (nextTonic > 127) {
                    nextTonic = nextTonic - 128;
                }
            } else if (Math.signum(nextTonic) < 0) {
                while (nextTonic < 127) {
                    nextTonic = nextTonic + 128;
                }
            }
            if (Math.signum(nextDom) >= 0) {
                while (nextDom > 127) {
                    nextDom = nextDom - 128;
                }
            } else if (Math.signum(nextDom) < 0) {
                while (nextDom < 127) {
                    nextDom = nextDom + 128;
                }
            }
            if (Math.signum(nextSub) >= 0) {
                while (nextSub > 127) {
                    nextSub = nextSub - 128;
                }
            } else if (Math.signum(nextSub) < 0) {
                while (nextSub < 127) {
                    nextSub = nextSub + 128;
                }
            }
            newSetting[i] = nextTonic;
            newSetting[i + 6] = nextDom;
            newSetting[i + 13] = nextSub;
        }
        return newSetting;
    }

    /**
     * Create a new SPEAC object containing only user defined notes
     *
     * @param name - the name of the new setting
     * @param setting - a SPEAC object containing the new setting
     * @return
     */
    public SPEAC addCustomSetting(String name, ArrayList<Integer> setting) {
        int[] newSetting = new int[setting.size()];
        for (int i = 0; i < newSetting.length; i++) {
            Integer temp = setting.get(i);
            if (Math.signum(temp) >= 0) {
                while (temp > 127) {
                    temp = temp - 128;
                }
            } else if (Math.signum(temp) < 0) {
                while (temp < 127) {
                    temp = temp + 128;
                }
            }
            newSetting[i] = temp;
        }
        return new SPEAC(newSetting, name);
    }

    /**
     * Reset SPEAC settings to default
     */
    public void resetRules() {
        instantiateWeights();
    }

    /**
     * Given the name of setting, remove it from the list of settings
     *
     * @param name - the name of the setting to be removed
     */
    public void removeSetting(String name) {
        for (int i = 0; i < speacWeights.size(); i++) {
            if (speacWeights.get(i).getName().equals(name)) {
                speacWeights.remove(i);
                break;
            }
        }
    }

    /**
     * Remove all settings from the list of settings
     */
    public void removeAllSettings() {
        speacWeights = new ArrayList<SPEAC>();
    }

    /**
     * Change the definition of a pre-existing rule with new definitions of the
     * tonic, dominant and subdominant
     *
     * @param name - the name of the setting to be changed
     * @param tonic
     * @param dominant
     * @param subdominant
     */
    public void changeSetting(String name, int tonic, int dominant, int subdominant) {
        for (SPEAC s : speacWeights) {
            if (s.getName().equals(name)) {
                int[] newSetting = addSetting(tonic, dominant, subdominant);
                s.setWeight(newSetting);
                break;
            }
        }
    }

    /**
     * Change the definition of a setting with completely custom set of notes
     *
     * @param name
     * @param setting
     */
    public void changeSettingCustom(String name, ArrayList<Integer> setting) {
        int[] newSetting = new int[setting.size()];
        for (SPEAC s : speacWeights) {
            if (s.getName().equals(name)) {
                for (int i = 0; i < newSetting.length; i++) {
                    Integer temp = setting.get(i);
                    if (Math.signum(temp) >= 0) {
                        while (temp > 127) {
                            temp = temp - 128;
                        }
                    } else if (Math.signum(temp) < 0) {
                        while (temp < 127) {
                            temp = temp + 128;
                        }
                    }
                    newSetting[i] = temp;
                }
                s.setWeight(newSetting);
                break;
            }
        }
    }

    /**
     * Get the range of starting pitch for a given list of measures. This method
     * was initially intended to return to the user the range of possible
     * initial measures to use for the start of a new song.
     *
     * @param measuresBySpeac
     * @return
     */
    public int[] getFirstPitchRange(ArrayList<Measure> measuresBySpeac) {
        int[] range = new int[measuresBySpeac.size()];
        int index = 0;
        for (Measure m : measuresBySpeac) {
            range[index] = m.getMeasureNotes().get(0).getPitch();
            index++;
        }
        Arrays.sort(range);
        return range;
    }

    /**
     * The main method for controlling the recombinatory process. The method is
     * passed the number of phrases that should be created to act as a library
     * of samples to use. It calls methods than recombine measures into phrases,
     * where these phrases are used to create the output song.
     *
     * @param measuresBySpeac - the measures to be used to create the output
     * @param numOfPhrases - the number of phrases to generate
     * @return -a new song
     */
    public ArrayList<Phrase> recombine(ArrayList<ArrayList<Measure>> measuresBySpeac, int numOfPhrases) {
        ArrayList<Phrase> phraseArray = new ArrayList<Phrase>();
        int range = 12;
        for (int i = 0; i < numOfPhrases; i++) {
            ArrayList<Measure> current = new ArrayList<Measure>();
            if (i >= ((numOfPhrases / 3) * 2)) {
                range = 24;
            }
            recombineMeasures(measuresBySpeac, current, range);
            Phrase p = new Phrase(current);
            phraseArray.add(p);
        }

        phraseArray = setSpeacPhrases(phraseArray);
        ArrayList<ArrayList<Phrase>> phrasesBySpeac = sortPhrasesBySpeac(phraseArray);

        ArrayList<Phrase> current = new ArrayList<Phrase>();
        recombinePhrases(phrasesBySpeac, current);

        return current;
    }

    /**
     * The ATN used for recombining measures. This method runs recursively until
     * a grammatically correct phrase has been produced.
     *
     * @param measuresBySpeac - the corpus of measures available for
     * recombination
     * @param current - the current phrase being constructed
     * @param range - the permitted range of difference in interval between the
     * last note of the previous measure and the first note of the next measure
     */
    public void recombineMeasures(ArrayList<ArrayList<Measure>> measuresBySpeac, ArrayList<Measure> current, int range) {
        int last = current.size() - 1;
        // if new phrase being created then insert statement
        if (current.isEmpty()) {
            Measure next = measuresBySpeac.get(0).get((int) (Math.random() * measuresBySpeac.get(0).size()));
            current.add(next);
            recombineMeasures(measuresBySpeac, current, range);
        } // base case
        else if (current.size() > 5) {
            // if previous measure isnt a consequent then remove
            if (!current.get(last).getSpeacWeight().startsWith("c")) {
                current.remove(last);
                // if measure prior to removed measure is either a statement or consequent remove
                if (current.get(last - 1).getSpeacWeight().startsWith("s") || current.get(last - 1).getSpeacWeight().startsWith("c")) {
                    current.remove(last - 1);
                    recombineMeasures(measuresBySpeac, current, range);
                } // grammatically correct phrase constructed
                else {
                    // define filtered list of measures to use to end phrase
                    ArrayList<ArrayList<Measure>> filter = new ArrayList<ArrayList<Measure>>();
                    filter.add(measuresBySpeac.get(4));
                    recombineMeasures(filter, current, range);
                }
            } else {
                return;
            }
        } else {
            Measure prev = current.get(last);
            String prevWeight = prev.getSpeacWeight();
            int lastNote = prev.getMeasureNotes().get((prev.getMeasureNotes().size() - 1)).getPitch();
            if (prevWeight.startsWith("s")) {
                ArrayList<Measure> possible = new ArrayList<Measure>();
                // if not last measure
                if (measuresBySpeac.size() > 1) {
                    possible.addAll(measuresBySpeac.get(1));
                    possible.addAll(measuresBySpeac.get(2));
                    possible.addAll(measuresBySpeac.get(3));
                    // filter possible measures by specified range
                    for (int i = 0; i < possible.size(); i++) {
                        Measure curr = possible.get(i);
                        ArrayList<Note> notes = curr.getMeasureNotes();
                        if (lastNote - notes.get(0).getPitch() > range || lastNote - notes.get(0).getPitch() < (-range)) {
                            possible.remove(i);
                        }
                    }
                } else {
                    possible.addAll(measuresBySpeac.get(0));
                    for (int i = 0; i < possible.size(); i++) {
                        Measure curr = possible.get(i);
                        ArrayList<Note> notes = curr.getMeasureNotes();
                        if (lastNote - notes.get(0).getPitch() > range || lastNote - notes.get(0).getPitch() < (-range)) {
                            possible.remove(i);
                        }
                    }
                }
                Measure next = possible.get((int) (Math.random() * possible.size()));
                current.add(next);
                recombineMeasures(measuresBySpeac, current, range);
            } else if (prevWeight.startsWith("p")) {
                ArrayList<Measure> possible = new ArrayList<Measure>();
                if (measuresBySpeac.size() > 1) {
                    possible.addAll(measuresBySpeac.get(0));
                    possible.addAll(measuresBySpeac.get(3));
                    possible.addAll(measuresBySpeac.get(4));
                    for (int i = 0; i < possible.size(); i++) {
                        Measure curr = possible.get(i);
                        ArrayList<Note> notes = curr.getMeasureNotes();
                        if (lastNote - notes.get(0).getPitch() > range || lastNote - notes.get(0).getPitch() < (-range)) {
                            possible.remove(i);
                        }
                    }
                } else {
                    possible.addAll(measuresBySpeac.get(0));
                    for (int i = 0; i < possible.size(); i++) {
                        Measure curr = possible.get(i);
                        ArrayList<Note> notes = curr.getMeasureNotes();
                        if (lastNote - notes.get(0).getPitch() > range || lastNote - notes.get(0).getPitch() < (-range)) {
                            possible.remove(i);
                        }
                    }
                }
                Measure next = possible.get((int) (Math.random() * possible.size()));
                current.add(next);
                recombineMeasures(measuresBySpeac, current, range);
            } else if (prevWeight.startsWith("e")) {
                ArrayList<Measure> possible = new ArrayList<Measure>();
                if (measuresBySpeac.size() > 1) {
                    possible.addAll(measuresBySpeac.get(0));
                    possible.addAll(measuresBySpeac.get(1));
                    possible.addAll(measuresBySpeac.get(3));
                    possible.addAll(measuresBySpeac.get(4));
                    for (int i = 0; i < possible.size(); i++) {
                        Measure curr = possible.get(i);
                        ArrayList<Note> notes = curr.getMeasureNotes();
                        if (lastNote - notes.get(0).getPitch() > range || lastNote - notes.get(0).getPitch() < (-range)) {
                            possible.remove(i);
                        }
                    }
                } else {
                    possible.addAll(measuresBySpeac.get(0));
                    for (int i = 0; i < possible.size(); i++) {
                        Measure curr = possible.get(i);
                        ArrayList<Note> notes = curr.getMeasureNotes();
                        if (lastNote - notes.get(0).getPitch() > range || lastNote - notes.get(0).getPitch() < (-range)) {
                            possible.remove(i);
                        }
                    }
                }
                Measure next = possible.get((int) (Math.random() * possible.size()));
                current.add(next);
                recombineMeasures(measuresBySpeac, current, range);
            } else if (prevWeight.startsWith("a")) {
                ArrayList<Measure> possible = new ArrayList<Measure>();
                if (measuresBySpeac.size() > 1) {
                    possible.addAll(measuresBySpeac.get(2));
                    possible.addAll(measuresBySpeac.get(4));
                    for (int i = 0; i < possible.size(); i++) {
                        Measure curr = possible.get(i);
                        ArrayList<Note> notes = curr.getMeasureNotes();
                        if (lastNote - notes.get(0).getPitch() > range || lastNote - notes.get(0).getPitch() < (-range)) {
                            possible.remove(i);
                        }
                    }
                } else {
                    possible.addAll(measuresBySpeac.get(0));
                    for (int i = 0; i < possible.size(); i++) {
                        Measure curr = possible.get(i);
                        ArrayList<Note> notes = curr.getMeasureNotes();
                        if (lastNote - notes.get(0).getPitch() > range || lastNote - notes.get(0).getPitch() < (-range)) {
                            possible.remove(i);
                        }
                    }
                }
                Measure next = possible.get((int) (Math.random() * possible.size()));
                current.add(next);
                recombineMeasures(measuresBySpeac, current, range);
            } else if (prevWeight.startsWith("c")) {
                ArrayList<Measure> possible = new ArrayList<Measure>();
                if (measuresBySpeac.size() > 1) {
                    possible.addAll(measuresBySpeac.get(0));
                    possible.addAll(measuresBySpeac.get(1));
                    possible.addAll(measuresBySpeac.get(2));
                    possible.addAll(measuresBySpeac.get(3));
                    for (int i = 0; i < possible.size(); i++) {
                        Measure curr = possible.get(i);
                        ArrayList<Note> notes = curr.getMeasureNotes();
                        if (lastNote - notes.get(0).getPitch() > range || lastNote - notes.get(0).getPitch() < (-range)) {
                            possible.remove(i);
                        }
                    }
                }
                Measure next = possible.get((int) (Math.random() * possible.size()));
                current.add(next);
                recombineMeasures(measuresBySpeac, current, range);
            }
        }
    }

    /**
     * The ATN used to recombine phrases. This method contains no additional
     * computation not already performed by the ATN used to recombine measures.
     *
     * @param phrasesBySpeac - the library of phrases available for
     * recombination
     * @param current - the current song being constructed
     */
    public void recombinePhrases(ArrayList<ArrayList<Phrase>> phrasesBySpeac, ArrayList<Phrase> current) {
        int last = current.size() - 1;
        if (current.isEmpty()) {
            Phrase next = phrasesBySpeac.get(0).get((int) (Math.random() * phrasesBySpeac.get(0).size()));
            current.add(next);
            recombinePhrases(phrasesBySpeac, current);
        } else if (current.size() > 5) {
            if (!current.get(last).getSpeacWeight().startsWith("c")) {
                current.remove(last);
                if (current.get(last - 1).getSpeacWeight().startsWith("s") || current.get(last - 1).getSpeacWeight().startsWith("c")) {
                    current.remove(last - 1);
                    recombinePhrases(phrasesBySpeac, current);
                } else {
                    ArrayList<ArrayList<Phrase>> filter = new ArrayList<ArrayList<Phrase>>();
                    filter.add(phrasesBySpeac.get(4));
                    recombinePhrases(filter, current);
                }
            } else {
                return;
            }
        } else {
            Phrase prev = current.get(last);
            String prevWeight = prev.getSpeacWeight();
            if (prevWeight.startsWith("s")) {
                ArrayList<Phrase> possible = new ArrayList<Phrase>();
                if (phrasesBySpeac.size() > 1) {
                    possible.addAll(phrasesBySpeac.get(1));
                    possible.addAll(phrasesBySpeac.get(2));
                    possible.addAll(phrasesBySpeac.get(3));
                } else {
                    possible.addAll(phrasesBySpeac.get(0));
                }
                Phrase next = possible.get((int) (Math.random() * possible.size()));
                current.add(next);
                recombinePhrases(phrasesBySpeac, current);
            } else if (prevWeight.startsWith("p")) {
                ArrayList<Phrase> possible = new ArrayList<Phrase>();
                if (phrasesBySpeac.size() > 1) {
                    possible.addAll(phrasesBySpeac.get(0));
                    possible.addAll(phrasesBySpeac.get(3));
                    possible.addAll(phrasesBySpeac.get(4));
                } else {
                    possible.addAll(phrasesBySpeac.get(0));
                }
                Phrase next = possible.get((int) (Math.random() * possible.size()));
                current.add(next);
                recombinePhrases(phrasesBySpeac, current);
            } else if (prevWeight.startsWith("e")) {
                ArrayList<Phrase> possible = new ArrayList<Phrase>();
                if (phrasesBySpeac.size() > 1) {
                    possible.addAll(phrasesBySpeac.get(0));
                    possible.addAll(phrasesBySpeac.get(1));
                    possible.addAll(phrasesBySpeac.get(3));
                    possible.addAll(phrasesBySpeac.get(4));
                } else {
                    possible.addAll(phrasesBySpeac.get(0));
                }
                Phrase next = possible.get((int) (Math.random() * possible.size()));
                current.add(next);
                recombinePhrases(phrasesBySpeac, current);
            } else if (prevWeight.startsWith("a")) {
                ArrayList<Phrase> possible = new ArrayList<Phrase>();
                if (phrasesBySpeac.size() > 1) {
                    possible.addAll(phrasesBySpeac.get(2));
                    possible.addAll(phrasesBySpeac.get(4));
                } else {
                    possible.addAll(phrasesBySpeac.get(0));
                }
                Phrase next = possible.get((int) (Math.random() * possible.size()));
                current.add(next);
                recombinePhrases(phrasesBySpeac, current);
            } else if (prevWeight.startsWith("c")) {
                ArrayList<Phrase> possible = new ArrayList<Phrase>();
                if (phrasesBySpeac.size() > 1) {
                    possible.addAll(phrasesBySpeac.get(0));
                    possible.addAll(phrasesBySpeac.get(1));
                    possible.addAll(phrasesBySpeac.get(2));
                    possible.addAll(phrasesBySpeac.get(3));
                }
                Phrase next = possible.get((int) (Math.random() * possible.size()));
                current.add(next);
                recombinePhrases(phrasesBySpeac, current);
            }
        }
    }

    /**
     *
     * @param sigs
     * @return
     */
    public ArrayList<Signature> sigSetStartTimesAndChannels(ArrayList<Signature> sigs) {
        int channel = 1;
        long startTime = 0;
        for (Signature s : sigs) {
            for (Note n : s.getNotes()) {
                n.setStart(startTime);
                n.setChannel(channel);
                startTime += n.getDuration();
            }
            startTime = startTime + 1000;
        }
        return sigs;
    }

    /**
     * Sets the start times and channels a phrase
     *
     * @param measures - the phrase to be corrected
     * @return - the corrected phrase
     */
    public ArrayList<Measure> setStartTimesAndChannels(ArrayList<Measure> measures) {
        // emulate type 0 MIDI
        int channel = 1;
        for (Measure m : measures) {
            long startTime = 0;
            for (Note n : m.getMeasureNotes()) {
                n.setStart(startTime);
                n.setChannel(channel);
                startTime += n.getDuration();
            }
            channel++;
        }
        return measures;
    }

    public Phrase setStartTimesAndChannels(Phrase phrase) {
        int channel = 1;
        long startTime = 0;
        for (Note n : phrase.getNotes()) {
            n.setStart(startTime);
            n.setChannel(channel);
            startTime += n.getDuration();
        }
        channel++;

        return phrase;
    }

    /**
     * Sets the start times and channels of a song to emulate a Type 0 MIDI file
     *
     * @param phraseArray - the song
     * @return - start times and channel configured for the song
     */
    public ArrayList<Phrase> configureTypeZero(ArrayList<Phrase> phraseArray) {
        long startTime = 0;
        for (Phrase p : phraseArray) {
            for (Note n : p.getNotes()) {
                n.setStart(startTime);
                n.setChannel(1);
                startTime += n.getDuration();
            }
        }
        return phraseArray;
    }

    /**
     * Write the track-note structure of a song to a MIDI file
     *
     * @param measureArray
     * @param filename
     * @throws InvalidMidiDataException
     * @throws IOException
     */
    public void writeMeasuresToFile(ArrayList<ArrayList<Measure>> measureArray, String filename) throws InvalidMidiDataException, IOException {
        Sequence sequence = null;
        if (divType == Sequence.PPQ) {
            sequence = new Sequence(Sequence.PPQ, res);
        } else if (divType == Sequence.SMPTE_24) {
            sequence = new Sequence(Sequence.SMPTE_24, res);
        } else if (divType == Sequence.SMPTE_25) {
            sequence = new Sequence(Sequence.SMPTE_25, res);
        } else if (divType == Sequence.SMPTE_30) {
            sequence = new Sequence(Sequence.SMPTE_30, res);
        } else if (divType == Sequence.SMPTE_30DROP) {
            sequence = new Sequence(Sequence.SMPTE_30DROP, res);
        }
        for (ArrayList<Measure> track : measureArray) {
            addMeasuresToTrack(sequence, res, track);
        }
        int[] allowedTypes = MidiSystem.getMidiFileTypes(sequence);
        if (allowedTypes.length == 0) {
            System.out.println("Midi file not writable from sequence");
        } else {
            MidiSystem.write(sequence, allowedTypes[0], new File(filename));
        }
    }

    /**
     * Write the track-note structure of a song to a MIDI file
     *
     * @param phraseArray
     * @param filename
     * @throws InvalidMidiDataException
     * @throws IOException
     */
    public void writePhrasesToFile(ArrayList<ArrayList<Phrase>> phraseArray, String filename) throws InvalidMidiDataException, IOException {
        Sequence sequence = null;
        if (divType == Sequence.PPQ) {
            sequence = new Sequence(Sequence.PPQ, res);
        } else if (divType == Sequence.SMPTE_24) {
            sequence = new Sequence(Sequence.SMPTE_24, res);
        } else if (divType == Sequence.SMPTE_25) {
            sequence = new Sequence(Sequence.SMPTE_25, res);
        } else if (divType == Sequence.SMPTE_30) {
            sequence = new Sequence(Sequence.SMPTE_30, res);
        } else if (divType == Sequence.SMPTE_30DROP) {
            sequence = new Sequence(Sequence.SMPTE_30DROP, res);
        }
        for (ArrayList<Phrase> track : phraseArray) {
            addPhrasesToTrack(sequence, res, track);
        }
        int[] allowedTypes = MidiSystem.getMidiFileTypes(sequence);
        if (allowedTypes.length == 0) {
            System.out.println("Midi file not writable from sequence");
        } else {
            MidiSystem.write(sequence, allowedTypes[0], new File(filename));
        }
    }

    /**
     * Write track-note structure of a song to a MIDI file
     *
     * @param trackArray
     * @param filename
     * @throws InvalidMidiDataException
     * @throws IOException
     */
    public void writeNotesToFile(ArrayList<ArrayList<Note>> trackArray, String filename) throws InvalidMidiDataException, IOException {
        Sequence sequence = null;
        if (divType == Sequence.PPQ) {
            sequence = new Sequence(Sequence.PPQ, res);
        } else if (divType == Sequence.SMPTE_24) {
            sequence = new Sequence(Sequence.SMPTE_24, res);
        } else if (divType == Sequence.SMPTE_25) {
            sequence = new Sequence(Sequence.SMPTE_25, res);
        } else if (divType == Sequence.SMPTE_30) {
            sequence = new Sequence(Sequence.SMPTE_30, res);
        } else if (divType == Sequence.SMPTE_30DROP) {
            sequence = new Sequence(Sequence.SMPTE_30DROP, res);
        }
        for (ArrayList<Note> track : trackArray) {
            addNotesToTrack(sequence, res, track);
        }
        int[] allowedTypes = MidiSystem.getMidiFileTypes(sequence);
        if (allowedTypes.length == 0) {
            System.out.println("Midi file not writable from sequence");
        } else {
            MidiSystem.write(sequence, allowedTypes[0], new File(filename));
        }
    }

    /**
     * Write the list of objects passed as input to a file. The use of the
     * wildcard operator allows this method to take ArrayLists of any object as
     * input
     *
     * @param objects - the list of objects to be written to a file
     * @param filename
     * @throws InvalidMidiDataException
     * @throws IOException
     */
    public void writeListToFile(ArrayList<?> objects, String filename) throws InvalidMidiDataException, IOException {
        Sequence sequence = null;
        if (divType == Sequence.PPQ) {
            sequence = new Sequence(Sequence.PPQ, res);
        } else if (divType == Sequence.SMPTE_24) {
            sequence = new Sequence(Sequence.SMPTE_24, res);
        } else if (divType == Sequence.SMPTE_25) {
            sequence = new Sequence(Sequence.SMPTE_25, res);
        } else if (divType == Sequence.SMPTE_30) {
            sequence = new Sequence(Sequence.SMPTE_30, res);
        } else if (divType == Sequence.SMPTE_30DROP) {
            sequence = new Sequence(Sequence.SMPTE_30DROP, res);
        }
        Track track = sequence.createTrack();
        for (Object o : objects) {
            if (o instanceof Mordent) {
                Mordent mordent = (Mordent) o;
                for (Note n : mordent.getMordentNotes()) {
                    addNote(track, (int) n.getStart(), (int) n.getDuration(), n.getPitch(), n.getVelocity(), n.getChannel());
                }
            } else if (o instanceof Trill) {
                Trill trill = (Trill) o;
                for (Note n : trill.getTrillNotes()) {
                    addNote(track, (int) n.getStart(), (int) n.getDuration(), n.getPitch(), n.getVelocity(), n.getChannel());
                }
            } else if (o instanceof Phrase) {
                Phrase phrase = (Phrase) o;
                for (Note n : phrase.getNotes()) {
                    addNote(track, (int) n.getStart(), (int) n.getDuration(), n.getPitch(), n.getVelocity(), n.getChannel());
                }
            } else if (o instanceof Measure) {
                Measure measure = (Measure) o;
                for (Note n : measure.getMeasureNotes()) {
                    addNote(track, (int) n.getStart(), (int) n.getDuration(), n.getPitch(), n.getVelocity(), n.getChannel());
                }
            } else if (o instanceof Signature) {
                Signature signature = (Signature) o;
                for (Note n : signature.getNotes()) {
                    addNote(track, (int) n.getStart(), (int) n.getDuration(), n.getPitch(), n.getVelocity(), n.getChannel());
                }
            } else {
                System.out.println("Invalid list type passed (writeListToFile");
            }
        }
        int[] allowedTypes = MidiSystem.getMidiFileTypes(sequence);
        if (allowedTypes.length == 0) {
            System.err.println("No supported MIDI file types.");
        } else {
            MidiSystem.write(sequence, allowedTypes[1], new File(filename));
        }
    }

    /**
     * Writes a generated phrase to a MIDI file
     *
     * @param phrase
     * @param filename
     * @throws InvalidMidiDataException
     */
    public void writePhraseToFile(Phrase phrase, String filename) throws InvalidMidiDataException, IOException {
        Sequence sequence = null;
        if (divType == Sequence.PPQ) {
            sequence = new Sequence(Sequence.PPQ, res);
        } else if (divType == Sequence.SMPTE_24) {
            sequence = new Sequence(Sequence.SMPTE_24, res);
        } else if (divType == Sequence.SMPTE_25) {
            sequence = new Sequence(Sequence.SMPTE_25, res);
        } else if (divType == Sequence.SMPTE_30) {
            sequence = new Sequence(Sequence.SMPTE_30, res);
        } else if (divType == Sequence.SMPTE_30DROP) {
            sequence = new Sequence(Sequence.SMPTE_30DROP, res);
        }
        Track track = sequence.createTrack();
        for (Note n : phrase.getNotes()) {
            addNote(track, (int) n.getStart(), (int) n.getDuration(), n.getPitch(), n.getVelocity(), n.getChannel());
        }
        int[] allowedTypes = MidiSystem.getMidiFileTypes(sequence);
        if (allowedTypes.length == 0) {
            System.err.println("No supported MIDI file types.");
        } else {
            MidiSystem.write(sequence, allowedTypes[1], new File(filename));
        }
    }

    /**
     *
     * @param phraseArray
     * @throws InvalidMidiDataException
     * @throws MidiUnavailableException
     */
    public void play(ArrayList<Phrase> phraseArray) throws InvalidMidiDataException, MidiUnavailableException {
        Sequence sequence = null;
        if (divType == Sequence.PPQ) {
            sequence = new Sequence(Sequence.PPQ, res);
        } else if (divType == Sequence.SMPTE_24) {
            sequence = new Sequence(Sequence.SMPTE_24, res);
        } else if (divType == Sequence.SMPTE_25) {
            sequence = new Sequence(Sequence.SMPTE_25, res);
        } else if (divType == Sequence.SMPTE_30) {
            sequence = new Sequence(Sequence.SMPTE_30, res);
        } else if (divType == Sequence.SMPTE_30DROP) {
            sequence = new Sequence(Sequence.SMPTE_30DROP, res);
        }
        addPhrasesToTrack(sequence, res, phraseArray);
        Sequencer sequencer = MidiSystem.getSequencer();
        sequencer.open();
        Synthesizer synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());
        sequencer.setSequence(sequence);
        sequencer.setTempoInBPM(tempo);
        sequencer.start();
    }

    /**
     *
     * @param sequence
     * @param tempo
     * @param measures
     * @throws InvalidMidiDataException
     */
    public void addMeasuresToTrack(Sequence sequence, int tempo, ArrayList<Measure> measures) throws InvalidMidiDataException {
        Track track = sequence.createTrack();
        ShortMessage sm = new ShortMessage(ShortMessage.PROGRAM_CHANGE, 0, 0, 0);       // third argument is instrument; 0 - acoustic grand piano
        track.add(new MidiEvent(sm, 0));
        for (Measure m : measures) {
            for (Note n : m.getMeasureNotes()) {
                addNote(track, (int) n.getStart(), (int) n.getDuration(), n.getPitch(), n.getVelocity(), n.getChannel());
            }
        }
    }

    /**
     *
     * @param sequence
     * @param tempo
     * @param phrases
     * @throws InvalidMidiDataException
     */
    public void addPhrasesToTrack(Sequence sequence, int tempo, ArrayList<Phrase> phrases) throws InvalidMidiDataException {
        Track track = sequence.createTrack();
        ShortMessage sm = new ShortMessage(ShortMessage.PROGRAM_CHANGE, 0, 0, 0);       // third argument is instrument; 0 - acoustic grand piano
        track.add(new MidiEvent(sm, 0));
        for (Phrase p : phrases) {
            for (Note n : p.getNotes()) {
                addNote(track, (int) n.getStart(), (int) n.getDuration(), n.getPitch(), n.getVelocity(), n.getChannel());
            }
        }
    }

    /**
     *
     * @param sequence
     * @param tempo
     * @param notes
     * @throws InvalidMidiDataException
     */
    public void addNotesToTrack(Sequence sequence, int tempo, ArrayList<Note> notes) throws InvalidMidiDataException {
        Track track = sequence.createTrack();
        ShortMessage sm = new ShortMessage(ShortMessage.PROGRAM_CHANGE, 0, 0, 0);       // third argument is instrument; 0 - acoustic grand piano
        track.add(new MidiEvent(sm, 0));
        for (int i = 0; i < notes.size(); i++) {
            addNote(track, (int) notes.get(i).getStart(), (int) notes.get(i).getDuration(), notes.get(i).getPitch(), notes.get(i).getVelocity(), notes.get(i).getChannel());
        }
    }

    /**
     *
     * @param track
     * @param measure
     * @throws InvalidMidiDataException
     */
    public void addMeasure(Track track, Measure measure) throws InvalidMidiDataException {
        ArrayList<Note> notes = measure.getMeasureNotes();
        for (Note n : notes) {
            addNote(track, (int) n.getStart(), (int) n.getDuration(), n.getPitch(), n.getVelocity(), n.getChannel());
        }
    }

    /**
     *
     * @param track
     * @param startTick
     * @param tickLength
     * @param key
     * @param velocity
     * @param channel
     * @throws InvalidMidiDataException
     */
    public static void addNote(Track track, int startTick, int tickLength, int key, int velocity, int channel) throws InvalidMidiDataException {
        ShortMessage on = new ShortMessage();
        on.setMessage(ShortMessage.NOTE_ON, channel, key, velocity);
        ShortMessage off = new ShortMessage();
        off.setMessage(ShortMessage.NOTE_OFF, channel, key, velocity);
        track.add(new MidiEvent(on, startTick));
        track.add(new MidiEvent(off, startTick + tickLength));
    }

    /**
     *
     * @param chooser
     * @return
     */
    public String getFile(JFileChooser chooser) {
        String title = "src/";
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            title = title.concat(chooser.getSelectedFile().getName());
        }
        return title;
    }

    /**
     *
     * @return
     */
    public ArrayList<SPEAC> getSpeacWeights() {
        return speacWeights;
    }

    /**
     *
     * @param speacWeights
     */
    public void setSpeacWeights(ArrayList<SPEAC> speacWeights) {
        this.speacWeights = speacWeights;
    }
}
