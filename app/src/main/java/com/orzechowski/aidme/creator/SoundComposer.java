package com.orzechowski.aidme.creator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orzechowski.aidme.R;
import com.orzechowski.aidme.tutorial.mediaplayer.sound.database.TutorialSound;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class SoundComposer extends Fragment
{
    private SoundComposerAdapter mAdapter;
    private final List<TutorialSound> soundList = new LinkedList<>();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
    {
        FragmentActivity activity = requireActivity();
        mAdapter = new SoundComposerAdapter(activity);
        View view = inflater.inflate(R.layout.fragment_sound_composer, container, false);
        RecyclerView recycler = view.findViewById(R.id.new_sound_rv);
        recycler.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState)
    {
        Button addSoundButton = view.findViewById(R.id.new_sound_button);
        addSoundButton.setOnClickListener(v-> {
            soundList.add(new TutorialSound(0, 0, false, 0, 0, ""));
            mAdapter.setElementList(soundList);
        });
    }
}