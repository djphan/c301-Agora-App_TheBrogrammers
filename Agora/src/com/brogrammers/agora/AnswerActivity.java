package com.brogrammers.agora;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class AnswerActivity extends Activity implements Observer{
	
	private QuestionController qController = QuestionController.getController();
	private List<Question> qList;
	private AnswerAdapter aadapter;

	ListView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answer);
		

		//comment button
		//Button viewComment = (Button)findViewById(R.id.AnswerCommentsButton); // no longer used
		//viewComment.setOnClickListener(opencommentview);	
		

		// For testing:
		//Long qID = 0L; //TODO: get question ID out of Intent/Bundle
		
		//Question q = new Question("New Thunderwave", "Why is it OP?", null, new Author("Mudkip"));
		//Answer a = new Answer("New Thunderwave Answer",null,new Author("mudkip"));
		//q.addAnswer(a);
		
		qController.setObserver(this);
		//Long qid = qController.addQuestion("AnswerActivity live test title", "AnswerActivity live test body", null);
		//Long aid = qController.addAnswer("AnswerActivity live answer title", null, qid);
		final CountDownLatch signal = new CountDownLatch(1);
		try {
			signal.await(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			
		}
		
		
		//qList = qController.getQuestionById(qid);

	
		lv = (ListView)findViewById(R.id.AnswerListView);
		try {
			aadapter = new AnswerAdapter(null);
			lv.setAdapter(aadapter);
//			Toast.makeText(this," Set Answer Adapter", 0).show();
		} catch (NullPointerException e) {
			Toast.makeText(this, "AnswerActivity Nullptr in setting adapter", 0).show();	
		}
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.answer, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	//open comment view
	View.OnClickListener opencommentview = new View.OnClickListener() {	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(Agora.getContext(), CommentActivity.class);
			startActivity(intent);
		}
	};
		
	//remove these later, made for button testing. actual function is implemented in controller.
	public void upvote(){
		Toast.makeText(this, "upvote", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void update() {
		if (qList.size() == 0) {
			Toast.makeText(this, "AnswerActivity update() called, but qList is empty", 0);
		} else {
			aadapter.setQuestion(qList.get(0));
//		aadapter.notifyDataSetChanged();
			Toast.makeText(this, "AnswerActivity got update notification", 0).show();
		}
		
	}


}
