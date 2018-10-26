package com.zhang.example;

import java.util.ArrayList;
import java.util.List;

import com.zhang.example.bean.Help;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


/**
 * @Description: TODO(添加描述)
 * @author sz.jun.zhang@gmail.com
 * @date 2013-7-11 上午3:50:43
 * @version V1.0
 */
public class ExpandActivity extends ListActivity {

//	private HelpDao dao;
	private List<Help> helps = new ArrayList<Help>();
	private boolean[] mExpanded;
	
	private static final String NAME = "title";
	private static final String DESCR = "description detail,"
			+ "description detail description detail description detail"
			+ "description detail description detail description detail"
			+ "description detail description detail description detail"
			+ "description detail description detail description detail";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		dao = new HelpDao(this);
//		helps = dao.getAll();
		for (int i = 0; i < 30; i++) {
			Help h = new Help(NAME, DESCR);
			helps.add(h);
		}
		mExpanded = new boolean[helps.size()];
		for (int i = 0; i < helps.size(); i++) {
			mExpanded[i] = false;
		}
		setListAdapter(new SpeechListAdapter(this));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		((SpeechListAdapter) getListAdapter()).toggle(position);
	}

	// 定义一个LinearLayout 上下结构 下面的textview可显示可隐藏
	private class SpeechView extends LinearLayout {
		public SpeechView(Context context, String title, String dialogue,
				boolean expanded) {
			super(context);

			this.setOrientation(VERTICAL);

			// Here we build the child views in code. They could also have
			// been specified in an XML file.

			mTitle = new TextView(context);
			// mTitle.setTextColor(R.color.title);
			mTitle.setTextColor(Color.RED);
			mTitle.setTextSize(20);
			mTitle.setText(title);
			addView(mTitle, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			mDialogue = new TextView(context);
			mDialogue.setTextSize(15);
			mDialogue.setText(dialogue);
			addView(mDialogue, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			mDialogue.setVisibility(expanded ? VISIBLE : GONE);
		}

		/**
		 * Convenience method to set the title of a SpeechView
		 */
		public void setTitle(String title) {
			mTitle.setText(title);
		}

		/**
		 * Convenience method to set the dialogue of a SpeechView
		 */
		public void setDialogue(String words) {
			mDialogue.setText(words);
		}

		/**
		 * Convenience method to expand or hide the dialogue
		 */
		public void setExpanded(boolean expanded) {
			mDialogue.setVisibility(expanded ? VISIBLE : GONE);
		}

		private TextView mTitle;
		private TextView mDialogue;
	}

	private class SpeechListAdapter extends BaseAdapter {
		public SpeechListAdapter(Context context) {
			mContext = context;
		}

		/**
		 * The number of items in the list is determined by the number of
		 * speeches in our array.
		 * 
		 * @see android.widget.ListAdapter#getCount()
		 */
		public int getCount() {
			return helps.size();
		}

		/**
		 * Since the data comes from an array, just returning the index is
		 * sufficent to get at the data. If we were using a more complex data
		 * structure, we would return whatever object represents one row in the
		 * list.
		 * 
		 * @see android.widget.ListAdapter#getItem(int)
		 */
		public Object getItem(int position) {
			return position;
		}

		/**
		 * Use the array index as a unique id.
		 * 
		 * @see android.widget.ListAdapter#getItemId(int)
		 */
		public long getItemId(int position) {
			return position;
		}

		/**
		 * Make a SpeechView to hold each row.
		 * 
		 * @see android.widget.ListAdapter#getView(int, android.view.View,
		 *      android.view.ViewGroup)
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			Help help = helps.get(position);
			String name = help.getName();
			String desc = help.getDescr();
			SpeechView sv;
			if (convertView == null) {
				sv = new SpeechView(mContext, name, desc, mExpanded[position]);
			} else {
				sv = (SpeechView) convertView;
				sv.setTitle(name);
				sv.setDialogue(desc);
				sv.setExpanded(mExpanded[position]);
			}

			return sv;
		}

		public void toggle(int position) {
			mExpanded[position] = !mExpanded[position];
			notifyDataSetChanged();
		}

		/**
		 * Remember our context so we can use it when constructing views.
		 */
		private Context mContext;

	}

}
