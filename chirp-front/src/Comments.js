import React, { useState } from 'react';

const Comment = ({ profilePicture, name, date, text, reactions }) => {
  const [reactionCounts, setReactionCounts] = useState(reactions);

  const handleReactionClick = (index) => {
    const updatedCounts = [...reactionCounts];
    const reactionCount = updatedCounts[index].count;
    const isReacted = updatedCounts[index].isReacted;

    if (isReacted) {
      updatedCounts[index].count -= 1;
      updatedCounts[index].isReacted = false;
    } else {
      updatedCounts[index].count += 1;
      updatedCounts[index].isReacted = true;
    }

    setReactionCounts(updatedCounts);
  };

  return (
    <div className="comment">
      <div className="header">
        <img src={profilePicture} alt="Profile" className="profile-picture" />
        <div className="comment-info">
          <span className="comment-name">{name}</span>
          <span className="comment-date">{date}</span>
        </div>
      </div>
      <div className="content">
        <p>{text}</p>
      </div>
      <div className="reactions">
        {reactionCounts.map((reaction, index) => (
          <button
            key={index}
            className={`reaction-button ${reaction.isReacted ? 'active' : ''}`}
            onClick={() => handleReactionClick(index)}
          >
            {reaction.emoji}
            <span className="reaction-count">{reaction.count}</span>
          </button>
        ))}
      </div>
    </div>
  );
};

const Comments = ({ comments }) => {
  return (
    <div className="comments">
      {comments.map((comment, index) => (
        <Comment
          key={index}
          profilePicture={comment.profilePicture}
          name={comment.name}
          date={comment.date}
          text={comment.text}
          reactions={comment.reactions}
        />
      ))}
    </div>
  );
};

export default Comments;
