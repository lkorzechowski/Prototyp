package com.orzechowski.aidme.creator.initial.imagebrowser

import android.content.ContentUris
import android.database.ContentObserver
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.orzechowski.aidme.R

class ImageBrowserLoader(val mCallback: ActivityCallback) : Fragment(),
    ImageBrowserAdapter.FragmentCallback
{
    private var mImageBrowserAdapter = ImageBrowserAdapter(this)
    private val mContentObserver: ContentObserver = object : ContentObserver(null) {
        override fun onChange(selfChange: Boolean) {
            setAdapterImages()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?):
            View
    {
        val view = inflater.inflate(R.layout.fragment_image_browser, container, false)
        val imageRecycler: RecyclerView = view.findViewById(R.id.image_recycler_grid)
        imageRecycler.layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
        imageRecycler.adapter = mImageBrowserAdapter
        requireActivity().contentResolver.registerContentObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, mContentObserver
        )
        setAdapterImages()
        return view
    }

    private fun setAdapterImages()
    {
        mImageBrowserAdapter.setElementList(loadImages())
    }

    private fun loadImages(): List<Image>
    {
        val photos = mutableListOf<Image>()
        val uri = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        requireActivity().contentResolver.query(uri, arrayOf(MediaStore.Images.Media._ID),
            null, null, null
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while(cursor.moveToNext()) {
                photos.add(Image(ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getLong(idColumn))))
            }
            return photos.toList()
        } ?: return listOf()
    }

    override fun onDestroy()
    {
        super.onDestroy()
        requireActivity().contentResolver.unregisterContentObserver(mContentObserver)
    }

    override fun imageClick(image: Image)
    {
        mCallback.imageSubmitted(image)
    }

    interface ActivityCallback
    {
        fun imageSubmitted(image: Image)
    }
}
