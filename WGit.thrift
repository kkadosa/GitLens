namespace java WGit
namespace py WGit

enum Trigger {
	ACCESS_1,
	ACCESS_2,
	PRE_GIT,
	POST_GIT
}

enum Modes {
	R,
	W,
	plus,
	C,
	D,
	WM,
	plusM,
	CM,
	DM,
	GitRecievePack,
	GitUploadPack,
	GitUploadArchive
}

struct ServerResponse {
	1: string Response,
	2: i64 ReturnValue
}

service WGitService {
	ServerResponse AnswerAccess1(1: string repoName, 2: string userName, 3: Modes mode, 4: string result),
	ServerResponse AnswerPreGit(1: string repoName, 2: string userName, 3: Modes mode, 4: Modes command),
	ServerResponse AnswerAccess2(1: string repoName, 2: string userName, 3: Modes mode, 4: string ref, 5: string result, 6: string oldCommit, 7: string newCommit),
	ServerResponse AnswerPostGit(1: string repoName, 2: string userName, 3: Modes mode, 4: Modes command)
}