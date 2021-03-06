package com.brogrammers.agora.views;

import java.util.List;

import com.brogrammers.agora.Agora;
import com.brogrammers.agora.Observer;
import com.brogrammers.agora.R;
import com.brogrammers.agora.R.id;
import com.brogrammers.agora.R.layout;
import com.brogrammers.agora.R.menu;
import com.brogrammers.agora.data.QuestionController;
import com.brogrammers.agora.model.Answer;
import com.brogrammers.agora.model.Comment;
import com.brogrammers.agora.model.Question;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
/**
 * Comment activity for displaying a list of comments corresponding to a question/answer.
 * @author Group02
 *
 */
public class CommentActivity extends Activity implements Observer {
	private long qid;
	private long aid;
	private List<Question> qList;
	private List<Comment> cList;
	private CommentAdapter cadapter;
	private QuestionController controller;

	private ListView lv;
	/**
	 * Retrieves question id or answer id from intent. 
	 * Then creates post comment button layout and the listview.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);

		Button postComment = (Button) findViewById(R.id.CommentPostButton);
		postComment.setOnClickListener(postcomment);

		qid = getIntent().getLongExtra("qid", 0L);
		aid = getIntent().getLongExtra("aid", 0L);

		controller = QuestionController.getController();
		controller.setObserver(this);
		qList = controller.getQuestionById(qid);
		if (aid == 0L) {
			cadapter = new CommentAdapter(qList.get(0));
		} else {
			cadapter = new CommentAdapter(qList.get(0).getAnswerByID(aid));
		}
		lv = (ListView) findViewById(R.id.CommentListView);
		lv.setAdapter(cadapter);
		
		LayoutInflater inflater = getLayoutInflater();
		View emptyView = inflater.inflate(R.layout.empty_comments, null);
		lv.setEmptyView(emptyView);
		((ViewGroup)lv.getParent()).addView(emptyView);
	}
	/**
	 * Called when question/answer list is populated. retrieves new set of comments from question/answer.
	 */
	@Override
	public void update() {
		if (qList.size() > 0) {
			if (aid == 0L) {
		
				Question q = qList.get(0);
				cadapter = new CommentAdapter(q);
				try {
					lv.setAdapter(cadapter);
				} catch (NullPointerException e) {
				}
			
			} else { // we're in answer comments
				Question q = qList.get(0);
				Answer a = q.getAnswerByID(aid);
				cadapter = new CommentAdapter(a);
				try {
					lv.setAdapter(cadapter);
				} catch (NullPointerException e) {
				}
			}
		} else {
			Toast.makeText(this, "qList empty onUpdate", 0).show();
		} 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comment, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(Agora.getContext(), UserPrefActivity.class);
			startActivity(intent);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * When post comment button clicked on. Posts comment to via corresponding question/answer id.
	 */
	View.OnClickListener postcomment = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			EditText commentBody = (EditText) findViewById(R.id.CommentEditText);
    		String body = commentBody.getText().toString();
			CheckBox locationBox = (CheckBox) findViewById(R.id.addLocationCommentBox);

			//
			if (aid == 0) { // adding comment to question 
				try {
					if (locationBox.isChecked()){
						controller.addComment(body, qid, null, true);
						Toast.makeText(Agora.getContext(), "Comment added!", 0).show();
					}
					else{
						controller.addComment(body, qid, null, false);
						Toast.makeText(Agora.getContext(), "Comment added!", 0).show();

					}
				} catch (NullPointerException e) {
				}
			} else { // adding comment to answer
				try {
					if (locationBox.isChecked()){
						controller.addComment(body, qid, aid, true);
						Toast.makeText(Agora.getContext(), "Comment added!", 0).show();
					} else{
						controller.addComment(body, qid, aid, false);
						Toast.makeText(Agora.getContext(), "Comment added!", 0).show();
					}
				} catch (NullPointerException e) {
				}
			}
    	 
			finish();
		}
	};

}