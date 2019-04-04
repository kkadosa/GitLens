namespace java hu.bme.mit.gitlens
namespace py hu.bme.mit.gitlens

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
	GIT_RECIEVE_PACK,
	GIT_UPLOAD_PACK,
	GIT_UPLOAD_ARCHIVE
}

struct ServerResponse {
	1: string Response,
	2: i64 ReturnValue
}

service GitLensService {
	ServerResponse answerAccess1(1: string repoName, 2: string userName, 3: Modes mode, 4: string result),
	ServerResponse answerPreGit(1: string repoName, 2: string userName, 3: Modes mode, 4: Modes command),
	ServerResponse answerAccess2(1: string repoName, 2: string userName, 3: Modes mode, 4: string ref, 5: string result, 6: string oldCommit, 7: string newCommit),
	ServerResponse answerPostGit(1: string repoName, 2: string userName, 3: Modes mode, 4: Modes command)
}