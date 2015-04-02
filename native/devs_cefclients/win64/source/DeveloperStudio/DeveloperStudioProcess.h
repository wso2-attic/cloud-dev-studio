#include <string.h>

class DeveloperStudioProcess
{
private:	
	int serverPort;
	std::string url;
	std::string basePath;

public:
	DeveloperStudioProcess();
	~DeveloperStudioProcess();
	std::string GetUrl();
	std::string GetBasePath();
	int GetServerPort();
	int Start();
	int Stop();
};