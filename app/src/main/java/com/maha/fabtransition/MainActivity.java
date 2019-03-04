package com.maha.fabtransition;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.ArcMotion;
import android.support.transition.ChangeBounds;
import android.support.transition.Fade;
import android.support.transition.Scene;
import android.support.transition.Slide;
import android.support.transition.Transition;
import android.support.transition.TransitionListenerAdapter;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

   private ViewGroup mSceneRoot;

   @Override
   protected void onCreate( Bundle savedInstanceState ) {
      super.onCreate( savedInstanceState );
      setContentView( R.layout.activity_main );

      setSupportActionBar( ( Toolbar ) findViewById( R.id.toolbar ) );
      mSceneRoot = findViewById( R.id.sceneRoot );
      //show scene1 without animation
      showScene1( false );
   }

   private void showScene1( boolean animated ) {
      ViewGroup root = ( ViewGroup ) getLayoutInflater().inflate( R.layout.frame_one, null );
      FloatingActionButton fab = root.findViewById( R.id.fab );
      fab.setOnClickListener( v -> {
         showScene2();
      } );
      Scene scene = new Scene( mSceneRoot, root );
      Transition transition = animated ? getScene1Transition() : null;
      TransitionManager.go( scene, transition );
   }


   private void showScene2() {
      ViewGroup root = ( ViewGroup ) getLayoutInflater().inflate( R.layout.frame_three, null );
      View btnBack = root.findViewById( R.id.btnCancel );

      Spinner spinnerHr = root.findViewById( R.id.hr_spinner );
      Spinner spinnerMin = root.findViewById( R.id.min_spinner );
      Spinner spinnerSec = root.findViewById( R.id.sec_spinner );

      ArrayList<String> aHrList = new ArrayList<>();
      for( int i = 0; i <= 12; i++ ) {
         aHrList.add( "" + i );
      }

      ArrayAdapter<String> hrAdapter = new ArrayAdapter<>( this, android.R.layout.simple_spinner_item, aHrList );
      spinnerHr.setAdapter( hrAdapter );

      ArrayList<String> aMinList = new ArrayList<>();
      for( int i = 0; i <= 59; i++ ) {
         aMinList.add( "" + i );
      }

      ArrayAdapter<String> minAdapter = new ArrayAdapter<>( this, android.R.layout.simple_spinner_item, aMinList );
      spinnerMin.setAdapter( minAdapter );

      ArrayAdapter<String> secAdapter = new ArrayAdapter<>( this, android.R.layout.simple_spinner_item, aMinList );
      spinnerSec.setAdapter( secAdapter );

      btnBack.setOnClickListener( v -> {
         showScene1( true );
      } );

      Scene scene = new Scene( mSceneRoot, root );
      Transition transition = getScene2Transition();
      TransitionManager.go( scene, transition );
   }

   private Transition getScene2Transition() {
      TransitionSet set = new TransitionSet();

      //fab changes position
      ChangeBounds changeTransform = new ChangeBounds();
      changeTransform.addListener( new TransitionListenerAdapter() {
         @Override
         public void onTransitionEnd( @NonNull Transition transition ) {
            //hide fab button on the end of animation
            mSceneRoot.findViewById( R.id.fab ).setVisibility( View.INVISIBLE );
         }
      } );


      changeTransform.addTarget( R.id.fab );
      changeTransform.setDuration( 300 );
      //fab arc path
      ArcMotion arcMotion = new ArcMotion();
      arcMotion.setMaximumAngle( 45 );
      arcMotion.setMinimumHorizontalAngle( 90 );
      arcMotion.setMinimumVerticalAngle( 0 );
      changeTransform.setPathMotion( arcMotion );
      set.addTransition( changeTransform );

      //bg circular reveal animation starts
      CircularRevealTransition crt = new CircularRevealTransition();
      crt.addTarget( R.id.yellowBG );
      crt.setStartDelay( 200 );
      crt.setDuration( 600 );
      set.addTransition( crt );

      //buttons appear
      Fade fade = new Fade();
      fade.addTarget( R.id.btnBegin );
      fade.addTarget( R.id.btnCancel );
      fade.addTarget( R.id.text );
      fade.addTarget( R.id.center_layout );
      fade.addTarget( R.id.week_layout );
      fade.setStartDelay( 600 );
      set.addTransition( fade );

      //left buttons column slide to left
      Slide slide = new Slide( Gravity.LEFT );
      slide.addTarget( R.id.slideLeftContainer );
      set.addTransition( slide );
      return set;
   }

   private Transition getScene1Transition() {
      TransitionSet set = new TransitionSet();

      //buttons from scene2 fade out
      Fade fade = new Fade();
      fade.addTarget( R.id.btnBegin );
      fade.addTarget( R.id.btnCancel );
      fade.addTarget( R.id.text );
      fade.addTarget( R.id.center_layout );
      fade.addTarget( R.id.week_layout );
      set.addTransition( fade );

      //Circular Reveal collapse animation starts
      CircularRevealTransition crt = new CircularRevealTransition();
      crt.addTarget( R.id.yellowBG );
      crt.setDuration( 600 );
      set.addTransition( crt );

      //then fab button changes position
      ChangeBounds changeTransform = new ChangeBounds();
      changeTransform.addTarget( R.id.fab );
      changeTransform.setDuration( 300 );
      changeTransform.setStartDelay( 500 );
      //arc path
      ArcMotion arcMotion = new ArcMotion();
      arcMotion.setMaximumAngle( 45 );
      arcMotion.setMinimumHorizontalAngle( 90 );
      arcMotion.setMinimumVerticalAngle( 0 );
      changeTransform.setPathMotion( arcMotion );
      set.addTransition( changeTransform );

      //left buttons column slide in from left
      Slide slide = new Slide( Gravity.LEFT );
      slide.addTarget( R.id.slideLeftContainer );
      slide.setStartDelay( 500 );
      set.addTransition( slide );

      return set;
   }

}
