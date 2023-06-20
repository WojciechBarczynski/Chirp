import React, { useState } from 'react';
import './CommentInput.css'

const CommentInput = ({ onSubmit }) => {
  const [commentText, setCommentText] = useState('');

  const handleTextChange = (event) => {
    setCommentText(event.target.value);
  };

  const handleSubmit = () => {
    if (commentText.trim() !== '') {
      onSubmit(commentText);
      setCommentText('');
    }
  };

  const handleReset = () => {
    setCommentText('');
  };

  return (
    <div className="comment-input">
      <textarea
        placeholder="Write a comment..."
        value={commentText}
        onChange={handleTextChange}
      />
      <div className="button-group">
        <button onClick={handleSubmit}>Submit</button>
        <button onClick={handleReset}>Reset</button>
      </div>
    </div>
  );
};

export default CommentInput;
