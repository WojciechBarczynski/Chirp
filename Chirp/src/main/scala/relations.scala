enum FollowLevel:
  case AllPosts, PopularPosts, RandomPosts, Blocked

case class Follows(level: FollowLevel)

enum ReactionType:
  case Like, Love, Haha, Wow, Sad, Angry

case class ReactedTo(reaction_type: ReactionType)

case class Shared(datetime: DateTime)

case class CreatedBy(datetime: DateTime)

case class Subscribes(level: FollowLevel)

case class CommentedOn()