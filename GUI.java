package core;

import Objects.FileData;
import Objects.Measure;
import Objects.Phrase;
import Objects.SPEAC;
import Objects.Signature;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Owner
 */
public final class GUI {

    String folder;
    Path path;
    EMI emi;
    File[] files;
    ArrayList<FileData> fileDataList;
    List<File> filesInFolder;
    ArrayList<ArrayList<Measure>> allMeasuresBySpeac;
    ArrayList<ArrayList<ArrayList<Measure>>> allMeasures;
    ArrayList<Phrase> newSong;
    ArrayList<ArrayList<ArrayList<Measure>>> allSongs;
    boolean mordents;
    boolean grace;
    boolean longs;
    boolean trills;

    public GUI() {
        this.files = null;
        this.fileDataList = new ArrayList<FileData>();
        this.allMeasuresBySpeac = new ArrayList<ArrayList<Measure>>();
        this.allMeasures = new ArrayList<ArrayList<ArrayList<Measure>>>();
        this.newSong = new ArrayList<Phrase>();
        this.allSongs = new ArrayList<ArrayList<ArrayList<Measure>>>();
        this.mordents = false;
        this.grace = false;
        this.longs = false;
        this.trills = false;
        this.emi = new EMI();
        createAndShowGUI();
    }

    public void createAndShowGUI() {

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("MIDI Files Only", "mid", "MID");
        chooser.addChoosableFileFilter(filter);
        chooser.setFileSelectionMode(0);

        // Set up main frame
        JFrame frame = new JFrame("EMI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setLocationRelativeTo(null);

        // frame for speac settings
        JFrame speacFrame = new JFrame("SPEAC Settings");
        speacFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        // Dimensions
        int panelWidth = 420;
        Dimension panelSize = new Dimension(panelWidth, 40);
        Dimension fieldSize = new Dimension(150, 30);
        Dimension buttonSize = new Dimension(200, 30);

        // Set up panels
        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setPreferredSize(new Dimension(460, 460));
        // Song one panel
        JPanel selectSongsPanel = new JPanel(new BorderLayout());
        selectSongsPanel.setPreferredSize(panelSize);
        // Song two panel        
        JPanel songTwoPanel = new JPanel(new BorderLayout());
        songTwoPanel.setPreferredSize(panelSize);
        // Disassembly panel
        JPanel disPanel = new JPanel();
        disPanel.setLayout(new BoxLayout(disPanel, BoxLayout.X_AXIS));
        disPanel.setPreferredSize(new Dimension(400, 100));
        // Create song panel
        JPanel createSongPanel = new JPanel(new BorderLayout());
        createSongPanel.setLayout(new BoxLayout(createSongPanel, BoxLayout.X_AXIS));
        createSongPanel.setPreferredSize(new Dimension(panelSize));
        // write file panel
        JPanel writeFilePanel = new JPanel(new BorderLayout());
        writeFilePanel.setPreferredSize(new Dimension(panelSize));
        // disassemble check box panel
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));
        // signature extraction panel
        JPanel signatureExtractionPanel = new JPanel();
        signatureExtractionPanel.setLayout(new BoxLayout(signatureExtractionPanel, BoxLayout.X_AXIS));
        signatureExtractionPanel.setPreferredSize(new Dimension(panelSize));
        // speac setting panel
        JPanel accessSPEACPanel = new JPanel();
        accessSPEACPanel.setLayout(new BoxLayout(accessSPEACPanel, BoxLayout.X_AXIS));
        accessSPEACPanel.setPreferredSize(new Dimension(panelSize));
        // create phrase panel
        JPanel createPhrasePanel = new JPanel();
        createPhrasePanel.setLayout(new BoxLayout(createPhrasePanel, BoxLayout.X_AXIS));
        createPhrasePanel.setPreferredSize(new Dimension(panelSize));
        // progress panel
        JPanel progressPanel = new JPanel(new FlowLayout());
        progressPanel.setPreferredSize(new Dimension(panelSize));

        // speac frame panels
        JPanel speacFrameControlPanel = new JPanel(new FlowLayout());
        speacFrameControlPanel.setPreferredSize(new Dimension(420, 200));

        JPanel speacButtonPanel = new JPanel();
        speacButtonPanel.setLayout(new BoxLayout(speacButtonPanel, BoxLayout.Y_AXIS));

        // Set up fields
        // select songs field
        JTextField selectSongsField = new JTextField();
        selectSongsField.setPreferredSize(fieldSize);
        // write file field
        JTextField writeFileField = new JTextField();
        writeFileField.setPreferredSize(fieldSize);
        // current process
        JTextField progressField = new JTextField();
        progressField.setPreferredSize(new Dimension(420, 30));
        progressField.setEditable(false);
        progressField.setHorizontalAlignment(SwingConstants.CENTER);

        // Set up check boxes
        JCheckBox removeTrills = new JCheckBox("Remove Trills");
        removeTrills.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox cb = (JCheckBox) e.getSource();
                if (cb.isSelected()) {
                    trills = true;
                } else {
                    trills = false;
                }
            }
        });

        JCheckBox removeMordents = new JCheckBox("Remove Mordents");
        removeMordents.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox cb = (JCheckBox) e.getSource();
                if (cb.isSelected()) {
                    mordents = true;
                } else {
                    mordents = false;
                }
            }
        });

        JCheckBox removeGraceNotes = new JCheckBox("Remove Grace Notes");
        removeGraceNotes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox cb = (JCheckBox) e.getSource();
                if (cb.isSelected()) {
                    grace = true;
                } else {
                    grace = false;
                }
            }
        });

        JCheckBox removeLongs = new JCheckBox("Remove Long Notes");
        removeLongs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox cb = (JCheckBox) e.getSource();
                if (cb.isSelected()) {
                    longs = true;
                } else {
                    longs = false;
                }
            }
        });

        // Set up buttons
        // Select song one
        JButton selectSongs = new JButton("Select Songs");
        selectSongs.setPreferredSize(buttonSize);
        selectSongs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getFiles(chooser);
                selectSongsField.setText(chooser.getCurrentDirectory().toString());
                progressField.setText("Songs successfully selected");
            }
        });

        // Disassemble button
        JButton disassemble = new JButton("Disassemble");
        disassemble.setPreferredSize(buttonSize);
        disassemble.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileDataList = new ArrayList<FileData>();
                ArrayList<File> fileZ = new ArrayList<File>();
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
                    for (Path file : stream) {
                        fileZ.add(file.toFile());
                    }
                } catch (IOException ex) {
                    System.err.println(ex);
                }

                allMeasuresBySpeac = new ArrayList<ArrayList<Measure>>();
                ArrayList<Measure> S = new ArrayList<Measure>();
                ArrayList<Measure> P = new ArrayList<Measure>();
                ArrayList<Measure> E = new ArrayList<Measure>();
                ArrayList<Measure> A = new ArrayList<Measure>();
                ArrayList<Measure> C = new ArrayList<Measure>();
                allMeasuresBySpeac.add(S);
                allMeasuresBySpeac.add(P);
                allMeasuresBySpeac.add(E);
                allMeasuresBySpeac.add(A);
                allMeasuresBySpeac.add(C);

                for (File f : fileZ) {
                    if (f.isFile()) {
                        FileData filedata = emi.getFileData(f);
                        emi.clarifyData(filedata, mordents, grace, trills, longs);
                        fileDataList.add(filedata);
                        ArrayList<ArrayList<Measure>> measures = emi.getMeasures(filedata.getNoteArray());
                        ArrayList<Measure> songAsTrack = emi.findChords(measures);
                        filedata.setMeasureArray(measures);
                        filedata.setSongAsSingleTrack(songAsTrack);
                        songAsTrack = emi.setSpeacMeasures(songAsTrack);
                        ArrayList<ArrayList<Measure>> mbys = emi.sortMeasuresBySpeac(songAsTrack);
                        int i = 0;
                        for (ArrayList<Measure> set : mbys) {
                            allMeasuresBySpeac.get(i).addAll(set);
                            i++;
                        }
                    }
                }
                progressField.setText("Songs successfully disassembled");
            }
        });

        // Use two songs to create new song
        JButton createSong = new JButton("Create Song");
        createSong.setPreferredSize(buttonSize);
        createSong.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int numOfPhrases = 0;
                numOfPhrases = Integer.parseInt(JOptionPane.showInputDialog(speacFrameControlPanel, "# of phrases to create:", "Create Song", JOptionPane.INFORMATION_MESSAGE));
                newSong = emi.recombine(allMeasuresBySpeac, numOfPhrases);
                newSong = emi.configureTypeZero(newSong);
                progressField.setText("Song created");
            }
        });

        JButton createPhrase = new JButton("Create Phrase");
        createPhrase.setPreferredSize(buttonSize);
        createPhrase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Measure> current = new ArrayList<Measure>();
                emi.recombineMeasures(allMeasuresBySpeac, current, 12);
                Phrase p = new Phrase(current);
                p = emi.setStartTimesAndChannels(p);
                try {
                    emi.writePhraseToFile(p, "newPhrase.mid");
                } catch (InvalidMidiDataException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                progressField.setText("Phrase created");
            }
        });

        // Write output song to file
        JButton extractSignature = new JButton("Extract Signature");
        extractSignature.setPreferredSize(buttonSize);
        extractSignature.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //int patternSize, int benchmark, int threshold, int intervalAllowance, int directionAllowance, int lengthAllowance
                int patternSize = 0;
                int benchmark = 0;
                int threshold = 0;
                int intervalAllowance = 0;
                int lengthAllowance = 0;
                patternSize = Integer.parseInt(JOptionPane.showInputDialog(speacFrameControlPanel, "Pattern Size:", "Extract Signature", JOptionPane.INFORMATION_MESSAGE));
                benchmark = Integer.parseInt(JOptionPane.showInputDialog(speacFrameControlPanel, "Benchmark:", "Extract Signature", JOptionPane.INFORMATION_MESSAGE));
                threshold = Integer.parseInt(JOptionPane.showInputDialog(speacFrameControlPanel, "Threshold: ", "Extract Signature", JOptionPane.INFORMATION_MESSAGE));
                intervalAllowance = Integer.parseInt(JOptionPane.showInputDialog(speacFrameControlPanel, "Interval Allowance: ", "Extract Signature", JOptionPane.INFORMATION_MESSAGE));
                lengthAllowance = Integer.parseInt(JOptionPane.showInputDialog(speacFrameControlPanel, "Length Allowance: ", "Extract Signature", JOptionPane.INFORMATION_MESSAGE));

                fileDataList = emi.matchPatterns(fileDataList, patternSize, benchmark, threshold, intervalAllowance, lengthAllowance);
                ArrayList<Signature> sigs = new ArrayList<Signature>();
                for (FileData filedata : fileDataList) {
                    for (Signature s : filedata.getIdentifiedSignatures()) {
                        if (!filedata.getIdentifiedSignatures().isEmpty()) {
                            sigs.addAll(filedata.getIdentifiedSignatures());
                        }
                    }
                }
                emi.sigSetStartTimesAndChannels(sigs);
                try {
                    emi.writeListToFile(sigs, "signatures.mid");
                } catch (InvalidMidiDataException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                progressField.setText("Signatures extracted");
            }
        });

        // Display signature extraction variables help
        JButton varHelp = new JButton("?");
        varHelp.setPreferredSize(new Dimension(50, 20));
        varHelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(controlPanel, "1 - Pattern Size: Number of measures used to define the length of the signature that is being searched for \n(recommended range 2 - 5).\n"
                        + "2 - Benchmark (%): The required amount of similarity a potential signature requires to pass similarity tests (lowest recommended minimum: 10%).\n"
                        + "3 - Threshold (%): The lowest bound for a potential signature to be accepted as a signature(lowest recommeneded minimum 10%). \n"
                        + "4 - Interval Allowance: The maximum allowed difference between two pattern values e.g. if set to 2, the patterns (2 ,4 ,6) and (5, 7, 9) would \nfail but (2, 4, 6) and (4, 5, 8) would pass.\n"
                        + "5 - Length Allowance: The maximum allowed difference between a potential signature and the pattern it is being tested against e.g. if set to 2, \nthe patterns (1, 2, 3) and (1, 2, 3, 4, 5, 6) would fail  but (1, 2, 3) and (1, 2, 3, 4, 5) would pass.\n"
                        + "Default Settings - Pattern Size: 2, Benchmark: 40, Threshold: 20, Interval Allowance: 2, Length Allowance: 2", "Signature Extraction Variables Explained ", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton accessSPEAC = new JButton("View/Define SPEAC settings");
        accessSPEAC.setPreferredSize(buttonSize);
        accessSPEAC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                speacFrame.setVisible(true);
            }
        });

        JButton writeSong = new JButton("Write Song To File");
        writeSong.setPreferredSize(buttonSize);
        writeSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    emi.writeListToFile(newSong, "output.mid");
                } catch (InvalidMidiDataException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                progressField.setText("Song written to file");
            }
        });

        // Play output song
        JButton playSong = new JButton("Play Song");
        playSong.setPreferredSize(buttonSize);
        playSong.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    emi.play(newSong);
                } catch (InvalidMidiDataException ex) {
                    Logger.getLogger(EMI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (MidiUnavailableException ex) {
                    Logger.getLogger(EMI.class.getName()).log(Level.SEVERE, null, ex);
                }
                progressField.setText("Finished playing");
            }
        });

        JButton addSetting = new JButton("Add Setting");
        addSetting.setPreferredSize(buttonSize);
        addSetting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<SPEAC> temp = emi.getSpeacWeights();
                String name = "";
                int tonic = 0;
                int dom = 0;
                int sub = 0;
                name = JOptionPane.showInputDialog(speacFrameControlPanel, "Name:", "Add Setting", JOptionPane.INFORMATION_MESSAGE);
                tonic = Integer.parseInt(JOptionPane.showInputDialog(speacFrameControlPanel, "Tonic:", "Add Setting", JOptionPane.INFORMATION_MESSAGE));
                dom = Integer.parseInt(JOptionPane.showInputDialog(speacFrameControlPanel, "Dominant: ", "Add Setting", JOptionPane.INFORMATION_MESSAGE));
                sub = Integer.parseInt(JOptionPane.showInputDialog(speacFrameControlPanel, "Subdominant: ", "Add Setting", JOptionPane.INFORMATION_MESSAGE));
                temp.add(emi.addSetting(name, tonic, dom, sub));
                emi.setSpeacWeights(temp);
                progressField.setText("Setting added");
            }
        });

        JButton addCustomSetting = new JButton("Add Custom Setting");
        addCustomSetting.setPreferredSize(buttonSize);
        addCustomSetting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<SPEAC> temp = emi.getSpeacWeights();
                ArrayList<Integer> values = new ArrayList<Integer>();
                String settings = "";
                String name = "";
                name = JOptionPane.showInputDialog(speacFrameControlPanel, "Name:", "Add Custom Setting", JOptionPane.INFORMATION_MESSAGE);
                settings = JOptionPane.showInputDialog(speacFrameControlPanel, "Setting:", "Add Custom Setting", JOptionPane.INFORMATION_MESSAGE);
                String[] stringArray = settings.split(" ");
                for (int i = 0; i < stringArray.length; i++) {
                    values.add(Integer.parseInt(stringArray[i]));
                }
                temp.add(emi.addCustomSetting(name, values));
                progressField.setText("Setting added");
            }
        });

        JButton changeSetting = new JButton("Change Setting");
        changeSetting.setPreferredSize(buttonSize);
        changeSetting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int tonic = 0;
                int dom = 0;
                int sub = 0;
                String name = "";
                name = JOptionPane.showInputDialog(speacFrameControlPanel, "Name:", "Change Setting", JOptionPane.INFORMATION_MESSAGE);
                tonic = Integer.parseInt(JOptionPane.showInputDialog(speacFrameControlPanel, "Tonic:", "Change Setting", JOptionPane.INFORMATION_MESSAGE));
                dom = Integer.parseInt(JOptionPane.showInputDialog(speacFrameControlPanel, "Dominant: ", "Change Setting", JOptionPane.INFORMATION_MESSAGE));
                sub = Integer.parseInt(JOptionPane.showInputDialog(speacFrameControlPanel, "Subdominant: ", "Change Setting", JOptionPane.INFORMATION_MESSAGE));
                emi.changeSetting(name, tonic, dom, sub);
                progressField.setText("Setting changed");
            }
        });

        JButton changeSettingCustom = new JButton("Change Setting (Custom)");
        changeSettingCustom.setPreferredSize(buttonSize);
        changeSettingCustom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Integer> values = new ArrayList<Integer>();
                String settings = "";
                String name = "";
                name = JOptionPane.showInputDialog(speacFrameControlPanel, "Name:", "Change Setting (Custom)", JOptionPane.INFORMATION_MESSAGE);
                settings = JOptionPane.showInputDialog(speacFrameControlPanel, "Setting:", "Change Setting  (Custom)", JOptionPane.INFORMATION_MESSAGE);
                String[] stringArray = settings.split(" ");
                for (int i = 0; i < stringArray.length; i++) {
                    values.add(Integer.parseInt(stringArray[i]));
                }
                emi.changeSettingCustom(name, values);
                progressField.setText("Setting changed");
            }
        });

        JButton removeSetting = new JButton("Remove Setting");
        removeSetting.setPreferredSize(buttonSize);
        removeSetting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = "";
                name = JOptionPane.showInputDialog(speacFrameControlPanel, "Name:", "Remove Setting", JOptionPane.INFORMATION_MESSAGE);
                emi.removeSetting(name);
                progressField.setText("Setting removed");
            }
        });

        JButton removeAllSettings = new JButton("Remove All Settings");
        removeAllSettings.setPreferredSize(buttonSize);
        removeAllSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emi.removeAllSettings();
                progressField.setText("All settings removed");
            }
        });

        JButton resetSettings = new JButton("Reset Settings");
        resetSettings.setPreferredSize(buttonSize);
        resetSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emi.resetRules();
                progressField.setText("Settings reset to default");
            }
        });

        selectSongsPanel.add(selectSongs, BorderLayout.WEST);
        selectSongsPanel.add(selectSongsField, BorderLayout.EAST);

        disPanel.add(disassemble, BorderLayout.WEST);
        checkBoxPanel.add(removeTrills);
        checkBoxPanel.add(removeGraceNotes);
        checkBoxPanel.add(removeMordents);
        checkBoxPanel.add(removeLongs);
        disPanel.add(checkBoxPanel, BorderLayout.EAST);

        signatureExtractionPanel.add(extractSignature);
        signatureExtractionPanel.add(varHelp);

        accessSPEACPanel.add(accessSPEAC);

        createSongPanel.add(createSong);

        createPhrasePanel.add(createPhrase);

        writeFilePanel.add(writeSong, BorderLayout.WEST);
        writeFilePanel.add(writeFileField, BorderLayout.EAST);

        controlPanel.add(selectSongsPanel);
        //controlPanel.add(songTwoPanel);
        controlPanel.add(disPanel);
        controlPanel.add(signatureExtractionPanel);
        controlPanel.add(accessSPEACPanel);
        controlPanel.add(createPhrasePanel);
        controlPanel.add(createSongPanel);
        controlPanel.add(writeFilePanel);
        controlPanel.add(playSong);
        controlPanel.add(progressField);

        speacButtonPanel.add(addSetting);
        speacButtonPanel.add(addCustomSetting);
        speacButtonPanel.add(changeSetting);
        speacButtonPanel.add(changeSettingCustom);
        speacButtonPanel.add(removeSetting);
        speacButtonPanel.add(removeAllSettings);
        speacButtonPanel.add(resetSettings);

        speacFrameControlPanel.add(speacButtonPanel);

        speacFrame.add(speacFrameControlPanel);
        speacFrame.pack();

        frame.add(controlPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public void getFiles(JFileChooser chooser) {
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            folder = chooser.getCurrentDirectory().getPath();
            files = chooser.getCurrentDirectory().listFiles();
            path = chooser.getCurrentDirectory().toPath();
        }
    }

    public static void main(String[] args) {
        GUI g = new GUI();
    }

}
