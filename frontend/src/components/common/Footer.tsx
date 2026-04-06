export function Footer() {
  return (
    <footer className="bg-editor-sidebar border-t border-editor-border px-4 py-3 text-center text-xs text-gray-500">
      <p>
        Built with Spring Boot + React + Monaco Editor •{' '}
        <a
          href="https://github.com/mhtpsd/collaborative-code-editor"
          target="_blank"
          rel="noopener noreferrer"
          className="text-editor-accent hover:underline"
        >
          View on GitHub
        </a>
      </p>
    </footer>
  );
}
