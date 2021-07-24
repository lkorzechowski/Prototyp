package com.orzechowski.aidme.creator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.orzechowski.aidme.R;

public class InstructionComposer extends Fragment
{
    private InstructionComposerAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
    {
        FragmentActivity activity = requireActivity();
        mAdapter = new InstructionComposerAdapter(activity);
        return inflater.inflate(R.layout.fragment_instruction_composer, container, false);
    }
}
