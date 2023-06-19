package objects

enum ReactionType {
  case Like, Love, Haha, Wow, Sad, Angry
}

def parseReaction(reaction: String): Option[ReactionType] = {
  reaction match
    case "like" => Some(ReactionType.Like)
    case "love" => Some(ReactionType.Love)
    case "haha" => Some(ReactionType.Haha)
    case "wow"  => Some(ReactionType.Wow)
    case "sad" => Some(ReactionType.Sad)
    case "angry" => Some(ReactionType.Angry)
    case _other => None
}

def reactionTypeToString(reactionType: ReactionType): String = {
  reactionType match
    case ReactionType.Like => "like" 
    case ReactionType.Love => "love" 
    case ReactionType.Haha => "haha" 
    case ReactionType.Wow => "wow" 
    case ReactionType.Sad => "sad" 
    case ReactionType.Angry => "angry" 
}