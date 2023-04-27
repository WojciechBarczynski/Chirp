enum UserStatus:
  case Active, Blocked, Deleted

case class User(email: String, username: String, password_hash: String, photo: String, bio: String, status: UserStatus)

enum ResourceKind:
  case Photo, Link

case class Resource(kind: ResourceKind, url: String)

case class Post(content: String, resources: List[Resource])

case class Tag(name: String)
