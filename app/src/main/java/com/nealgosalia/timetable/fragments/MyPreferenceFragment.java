package com.nealgosalia.timetable.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.nealgosalia.timetable.R;
import com.nealgosalia.timetable.activities.PreferencesActivity;

import java.io.File;

/**
 * Created by men_in_black007 on 15/12/16.
 */

public class MyPreferenceFragment extends PreferenceFragment {

    private static final String TAG = "MyPreferenceFragment";
    private PreferencesActivity mActivity;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        mActivity = (PreferencesActivity) getActivity();
        Preference backup = findPreference("backup");
        Preference restore = findPreference("restore");
        backup.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (mActivity.isStoragePermissionGranted(getActivity())) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle("Backup Timetable");
                    alertDialog.setMessage("Do you want to backup the timetable?");
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String backupDBPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Timetable/";
                            final File subjectDB = new File(backupDBPath + "subject.db");
                            final File lectureDB = new File(backupDBPath + "lecture.db");
                            if (!(subjectDB.exists() || lectureDB.exists())) {
                                if (mActivity.exportDatabase("subject.db") && mActivity.exportDatabase("lecture.db")) {
                                    Toast.makeText(getActivity(), "Backup Successful!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "Please create a timetable before trying to export", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                AlertDialog overwriteDialog = new AlertDialog.Builder(getActivity())
                                        .setTitle("Warning!")
                                        .setMessage("Overwrite previous backup?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                subjectDB.delete();
                                                lectureDB.delete();
                                                if (mActivity.exportDatabase("subject.db") && mActivity.exportDatabase("lecture.db")) {
                                                    Toast.makeText(getActivity(), "Backup Successful!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getActivity(), "Please create a timetable before trying to export", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .create();
                                overwriteDialog.show();
                            }
                        }
                    });
                    alertDialog.setNegativeButton("No", null);
                    alertDialog.show();
                } else {
                    Toast.makeText(getActivity(), "No Permissions Granted", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        restore.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (mActivity.isStoragePermissionGranted(getActivity())) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle("Restore Timetable");
                    alertDialog.setMessage("Do you want to restore the timetable?");
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String backupDBPath = "data/data/com.nealgosalia.timetable/databases/";
                            final File subjectDB = new File(backupDBPath + "subject.db");
                            final File lectureDB = new File(backupDBPath + "lecture.db");
                            if (!(subjectDB.exists() || lectureDB.exists())) {
                                if (mActivity.importDatabase("subject.db") && mActivity.importDatabase("lecture.db")) {
                                    Toast.makeText(getActivity(), "Restore Successful!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "Backup not found!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                AlertDialog overwriteDialog = new AlertDialog.Builder(getActivity())
                                        .setTitle("Warning!")
                                        .setMessage("Overwrite current timetable?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                subjectDB.delete();
                                                lectureDB.delete();
                                                if (mActivity.importDatabase("subject.db") && mActivity.importDatabase("lecture.db")) {
                                                    Toast.makeText(getActivity(), "Restore Successful!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getActivity(), "Backup not found", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .create();
                                overwriteDialog.show();
                            }
                        }
                    });
                    alertDialog.setNegativeButton("No", null);
                    alertDialog.show();
                } else {
                    Toast.makeText(getActivity(), "No Permissions Granted", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }
}